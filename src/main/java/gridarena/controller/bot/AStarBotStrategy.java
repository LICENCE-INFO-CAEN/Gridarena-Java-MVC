package gridarena.controller.bot;

import gridarena.entity.Entity;
import gridarena.entity.consumable.Consumable;
import gridarena.entity.consumable.MedicalKit;
import gridarena.entity.consumable.AmmoKit;
import gridarena.entity.environment.Wall;
import gridarena.entity.explosive.Barrel;
import gridarena.entity.explosive.Mine;
import gridarena.entity.hero.Hero;
import gridarena.entity.hero.HeroFactory;
import gridarena.model.BattlefieldModel;

import java.util.*;

/**
 * Représente une stratégie de joueur robot intelligente utilisant l'algorithme de recherche de chemin A*.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class AStarBotStrategy implements BotStrategy {

    protected static final String[] moveDirections = {"up", "down", "left", "right"};
    protected static final String[] explosiveDirections = {"up", "down", "left", "right", "ld", "lu", "ru", "rd"};

    private static class Node implements Comparable<Node> {
        int x, y;
        int g; // coût du chemin depuis le départ
        int h; // heuristique (distance de Manhattan vers la cible)
        int f; // coût total
        Node parent;

        Node(int x, int y, int g, int h, Node parent) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
            this.f = g + h;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.f, o.f);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Node) {
                Node other = (Node) obj;
                return this.x == other.x && this.y == other.y;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    @Override
    public void actionStrategy(BattlefieldModel battlefieldProxy) {
        if (battlefieldProxy.getCurrentHero() == null) {
            // Par défaut, ajoute un héros de type Warrior
            battlefieldProxy.addHero(HeroFactory.getFactory("warrior"));
            return;
        }

        Hero hero = battlefieldProxy.getCurrentHero();
        Entity[][] grid = battlefieldProxy.getGrid();
        int size = grid.length;

        // 1. Combat immédiat : Attaque à la hache si un ennemi est adjacent
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < directions.length; i++) {
            int nx = hero.getX() + directions[i][0];
            int ny = hero.getY() + directions[i][1];
            if (battlefieldProxy.isValidPosition(nx, ny)) {
                if (grid[nx][ny] instanceof Hero && grid[nx][ny] != hero) {
                    battlefieldProxy.axAttack(hero, moveDirections[i]);
                    return;
                }
            }
        }

        // 2. Combat immédiat : Tir à distance si un ennemi est en ligne de mire et qu'on a des munitions
        if (hero.getAmmoRemaining() > 0) {
            String shootDir = this.isTargetInRange(battlefieldProxy, hero);
            if (!shootDir.equals("none")) {
                battlefieldProxy.shootHero(hero, shootDir);
                return;
            }
        }

        // 3. Sélection de la cible et Recherche de chemin A*
        List<int[]> path = null;

        // Cible prioritaire 1 : Kit de soin si les points de vie sont bas
        if (hero.getHealthRemaining() < 15) {
            int[] target = this.findNearestTarget(grid, hero, "health");
            if (target != null) {
                path = this.findPath(battlefieldProxy, grid, hero, target[0], target[1]);
            }
        }

        // Cible prioritaire 2 : Kit de munitions si les munitions sont basses
        if ((path == null || path.isEmpty()) && hero.getAmmoRemaining() < 5) {
            int[] target = this.findNearestTarget(grid, hero, "ammo");
            if (target != null) {
                path = this.findPath(battlefieldProxy, grid, hero, target[0], target[1]);
            }
        }

        // Cible par défaut : L'adversaire le plus proche
        if (path == null || path.isEmpty()) {
            int[] target = this.findNearestTarget(grid, hero, "opponent");
            if (target != null) {
                path = this.findPath(battlefieldProxy, grid, hero, target[0], target[1]);
            }
        }

        // 4. Déplacement si un chemin optimal A* est trouvé
        if (path != null && !path.isEmpty()) {
            int[] nextStep = path.get(0);
            String moveDir = this.getDirection(hero.getX(), hero.getY(), nextStep[0], nextStep[1]);
            if (moveDir != null) {
                battlefieldProxy.moveHero(hero, moveDir);
                return;
            }
        }

        // 5. Repli tactique si aucun chemin n'a été trouvé ou pas de cible
        String mineDir = this.findMineLocation(battlefieldProxy, hero);
        if (!mineDir.equals("none") && hero.getMineRemaining() > 0) {
            battlefieldProxy.addExplosive(hero, mineDir, "mine");
        } else if (hero.getShieldRemaining() > 0) {
            battlefieldProxy.activateShield(hero);
        } else {
            // Déplacement aléatoire dans une case sûre
            String randomMoveDir = this.findAvailableDirection(battlefieldProxy, hero, false);
            if (!randomMoveDir.equals("none")) {
                battlefieldProxy.moveHero(hero, randomMoveDir);
            }
        }
    }

    private int[] findNearestTarget(Entity[][] grid, Hero myHero, String targetType) {
        int myX = myHero.getX();
        int myY = myHero.getY();
        int minDistance = Integer.MAX_VALUE;
        int[] targetPos = null;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Entity e = grid[i][j];
                if (e == null) continue;

                boolean isMatch = false;
                if ("health".equals(targetType) && e instanceof MedicalKit) {
                    isMatch = true;
                } else if ("ammo".equals(targetType) && e instanceof AmmoKit) {
                    isMatch = true;
                } else if ("opponent".equals(targetType) && e instanceof Hero && e != myHero) {
                    isMatch = true;
                }

                if (isMatch) {
                    int dist = Math.abs(i - myX) + Math.abs(j - myY);
                    if (dist < minDistance) {
                        minDistance = dist;
                        targetPos = new int[]{i, j};
                    }
                }
            }
        }
        return targetPos;
    }

    private List<int[]> findPath(BattlefieldModel battlefieldProxy, Entity[][] grid, Hero myHero, int targetX, int targetY) {
        int size = grid.length;
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        boolean[][] closedSet = new boolean[size][size];

        int startX = myHero.getX();
        int startY = myHero.getY();

        openSet.add(new Node(startX, startY, 0, Math.abs(startX - targetX) + Math.abs(startY - targetY), null));

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.x == targetX && current.y == targetY) {
                List<int[]> path = new ArrayList<>();
                Node curr = current;
                while (curr.parent != null) {
                    path.add(0, new int[]{curr.x, curr.y});
                    curr = curr.parent;
                }
                return path;
            }

            closedSet[current.x][current.y] = true;

            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];

                if (battlefieldProxy.isValidPosition(nx, ny)) {
                    if (closedSet[nx][ny]) continue;

                    Entity e = grid[nx][ny];
                    // La case est traversable si elle est vide, si c'est un consommable,
                    // ou s'il s'agit de la cible elle-même (pour pouvoir l'atteindre),
                    // ou si c'est une mine posée par le bot lui-même.
                    boolean isWalkable = (e == null || e instanceof Consumable || (nx == targetX && ny == targetY));
                    if (e instanceof Mine) {
                        Mine mine = (Mine) e;
                        if (mine.getBelongsTo() == myHero) {
                            isWalkable = true;
                        }
                    }

                    if (!isWalkable) continue;

                    int nextG = current.g + 1;
                    int nextH = Math.abs(nx - targetX) + Math.abs(ny - targetY);
                    Node neighbor = new Node(nx, ny, nextG, nextH, current);

                    boolean skip = false;
                    for (Node openNode : openSet) {
                        if (openNode.x == nx && openNode.y == ny && openNode.g <= nextG) {
                            skip = true;
                            break;
                        }
                    }

                    if (!skip) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private String getDirection(int startX, int startY, int endX, int endY) {
        if (endX == startX - 1 && endY == startY) return "up";
        if (endX == startX + 1 && endY == startY) return "down";
        if (endX == startX && endY == startY - 1) return "left";
        if (endX == startX && endY == startY + 1) return "right";
        return null;
    }

    private String isTargetInRange(BattlefieldModel battlefieldProxy, Hero hero) {
        Entity[][] grid = battlefieldProxy.getGrid();
        int size = grid.length;
        List<String> locations = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int i = 0; i < directions.length; i++) {
            int x = hero.getX();
            int y = hero.getY();
            for (int j = 0; j < size; j++) {
                x += directions[i][0];
                y += directions[i][1];
                if (battlefieldProxy.isValidPosition(x, y)) {
                    if (grid[x][y] instanceof Wall) {
                        break;
                    } else if (grid[x][y] instanceof Hero || grid[x][y] instanceof Barrel) {
                        locations.add(moveDirections[i]);
                    }
                }
            }
        }

        if (!locations.isEmpty()) {
            Collections.shuffle(locations);
            return locations.get(0);
        }
        return "none";
    }

    private String findMineLocation(BattlefieldModel battlefieldProxy, Hero hero) {
        Entity[][] grid = battlefieldProxy.getGrid();
        int size = grid.length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1, -1}, {-1, -1}, {-1, 1}, {1, 1}};
        List<String> locations = new ArrayList<>();
        List<String> moves = new ArrayList<>();
        int x = hero.getX();
        int y = hero.getY();

        for (int i = 0; i < directions.length; i++) {
            int nx = x + directions[i][0];
            int ny = y + directions[i][1];
            if (battlefieldProxy.isValidPosition(nx, ny)) {
                if (grid[nx][ny] == null) {
                    if (i < 4) {
                        moves.add(explosiveDirections[i]);
                    }
                    locations.add(explosiveDirections[i]);
                }
            }
        }

        if (moves.size() > 1) {
            Collections.shuffle(locations);
            return locations.get(0);
        }
        return "none";
    }

    private String findAvailableDirection(BattlefieldModel battlefieldProxy, Hero hero, boolean state) {
        Entity[][] grid = battlefieldProxy.getGrid();
        int size = grid.length;
        List<String> locations = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int x = hero.getX();
        int y = hero.getY();

        for (int i = 0; i < directions.length; i++) {
            int nx = x + directions[i][0];
            int ny = y + directions[i][1];
            if (battlefieldProxy.isValidPosition(nx, ny)) {
                if (state && grid[nx][ny] instanceof Hero) {
                    locations.add(moveDirections[i]);
                } else if (!state && (grid[nx][ny] == null || grid[nx][ny] instanceof Consumable || grid[nx][ny] instanceof Mine)) {
                    locations.add(moveDirections[i]);
                }
            }
        }

        if (!locations.isEmpty()) {
            Collections.shuffle(locations);
            return locations.get(0);
        }
        return "none";
    }
}
