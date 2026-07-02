package gridarena.model;

import gridarena.entity.Entity;
import gridarena.entity.consumable.*;
import gridarena.entity.environment.Wall;
import gridarena.entity.explosive.*;
import gridarena.entity.hero.*;
import gridarena.model.fillgrid.*;
import gridarena.utils.AbstractListenableModel;

import java.util.*;

/**
 * Représente un champ de bataille.
 *
 * @author Florian Pépin.
 * @version 2.0
 */
public class Battlefield extends AbstractListenableModel implements BattlefieldModel {
    
    private FillStrategy fillStrategy;
    private Hero currentHero;
    private GroupHeroesArrayList heroes;
    private List<Bomb> bombs;
    private Entity[][] grid;
    private int size;
    
    public Battlefield(int size, int walls, int medicalKits, int ammoKits, int barrels, FillStrategy fillStrategy) {
        super();
        this.fillStrategy = fillStrategy;
        this.currentHero = null;
        this.heroes = new GroupHeroesArrayList();
        this.bombs = new ArrayList<>();
        this.grid = new Entity[size][size];
        this.size = this.grid.length;
        this.fillStrategy.fillGrid(this.grid, walls, medicalKits, ammoKits, barrels);
    }
    
    @Override
    public Hero getCurrentHero() {
        return this.currentHero;
    }
    
    @Override
    public GroupHeroesArrayList getHeroes() {
        return this.heroes;
    }
    
    @Override
    public Entity[][] getGrid() {
        return this.grid;
    }
    
    @Override
    public int getSize() {
        return this.size;
    }
    
    @Override
    public void setCurrentHero(int i) {
        if (i < this.heroes.getSize()) {
            this.currentHero = this.heroes.getHero(i);
            this.fireChange();
        }
    }
    
    @Override
    public Hero addHero(HeroFactory factory) {
        Hero hero = this.fillStrategy.fillGridWithHero(this.grid, factory);
        this.heroes.addHero(hero);
        this.fireChange();
        return hero;
    }

    @Override
    public boolean addExplosive(Hero h, String direction, String explosiveType) {
        int[] pos = this.wichDirection(direction);
        int x = h.getX()+pos[0];
        int y = h.getY()+pos[1];
        if(this.isPosition(x, y)) {
            Entity e = this.grid[x][y];
            if (e == null) {
                Explosive explosive = null;
                if ("mine".equals(explosiveType) && h.useMine()) {
                    explosive = new Mine(x, y, h);
                } else if ("bomb".equals(explosiveType) && h.useBomb()) {
                    explosive = new Bomb(x, y, h);
                }

                if (explosive == null) {
                    return false;
                } else {
                    if (explosive instanceof Bomb) this.bombs.add((Bomb)explosive);
                    this.grid[x][y] = explosive;
                    this.fireChange();
                    return true;
                }
            } else if (e instanceof Explosive) {
                this.detonate((Explosive) e);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean moveHero(Hero h, String direction) {
        int[] pos = this.wichDirection(direction);
        int x = h.getX() + pos[0];
        int y = h.getY() + pos[1];
        if (this.isPosition(x, y)) {
            Entity e = this.grid[x][y];
            if (e == null) {
                this.moveHeroToNewPosition(h, x, y);
                return true;
            } else if (e instanceof Explosive) {
                if (((Explosive)e).isWalkable()) {
                    this.detonate((Explosive) e);
                    this.moveHeroToNewPosition(h, x, y);
                    this.isHeroDead(h);
                    return true;    
                }
            } else if (e instanceof Consumable) {
                ((Consumable) e).useConsumable(h);
                this.moveHeroToNewPosition(h, x, y);
                return true;
            }
            return false;
        }
        return false;
    }
    
    @Override
    public boolean activateShield(Hero h) {
        int shieldCount = h.getShieldRemaining();
        if(shieldCount > 0) {
            h.setImmune(true);
            h.setShieldRemaining(shieldCount-1);
            return true;
        }
        return false;
    }
    
    /**
     * Décrémente de un le compteur des bombes et les fait exploser si nécessaire.
     */
    public void decrementBombs() {
        for (int i = 0; i < this.bombs.size(); i++) {
            boolean state = this.bombs.get(i).decrementeTimer();
            if (state) {
                this.detonate(this.bombs.get(i));
                this.bombs.remove(i);
                
            }
        }
    }

    /**
     * Détonation d'un explosif.
     * 
     * @param explosive est l'explosif qui va détoner.
     */
    private void detonate(Explosive explosive) {
        List<Entity> entities = this.nearestNeighborsEntity(explosive.getExplosionRadius(), explosive.getX(), explosive.getY());
        this.grid[explosive.getX()][explosive.getY()] = null;
        for (Entity entitie : entities) {
            if (entitie instanceof Hero) {
                explosive.explode((Hero) entitie);
                this.isHeroDead((Hero) entitie);
            }
        }
        this.fireChange();
    }
    
    /**
     * Recherche les entités les plus proches situés dans un rayon donné.
     * 
     * @param n Le rayon de recherche, exprimé en nombre de cases.
     * @param x Coordonné horizontale du point de recherche.
     * @param y Coordonné verticale du point de recherche.
     * @return Une liste contenant toutes les entités répondant aux critères de proximité.
     */
    private List<Entity> nearestNeighborsEntity(int n, int x, int y) {
        List<Entity> neighbors = new ArrayList<>();
        for (int i=0; i < this.size; i++) {
            for (int j=0; j < this.size; j++) {
                if (this.grid[i][j] != null) {
                    int diffX = Math.abs(this.grid[i][j].getX()-x);
                    int diffY = Math.abs(this.grid[i][j].getY()-y);
                    int maxi = Math.max(diffX, diffY);
                    if (maxi <= n) {
                        neighbors.add(this.grid[i][j]);
                    }
                }
            }
        }
        return neighbors;
    }
    
    @Override
    public boolean axAttack(Hero h, String d) {
        int[] direction = this.wichDirection(d);
        int x = h.getX()+direction[0];
        int y = h.getY()+direction[1];
        if(this.isPosition(x, y)) {
            Entity e = this.grid[x][y];
            if (e instanceof Wall) {
                return false;
            } else if (e instanceof Hero) {
                Hero hero = (Hero) e;
                if (hero.isImmune()) return true;
                h.axAttack(hero);
                this.isHeroDead(hero);
                this.fireChange();
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean shootHero(Hero h, String d) {
        h.setAmmoRemaining(h.getAmmoRemaining()-1);
        int[] direction = this.wichDirection(d);
        int x = h.getX();
        int y = h.getY();
        for (int i=0; i < this.size; i++) {
            x += direction[0];
            y += direction[1];
            if(this.isPosition(x, y)) {
                Entity e = this.grid[x][y];
                if (e instanceof Wall) {
                    return false;
                } else if (e instanceof Hero) {
                    Hero hero = (Hero) e;
                    if (hero.isImmune()) return true;
                    h.shootHero(hero);
                    this.isHeroDead(hero);
                    this.fireChange();
                    return true;
                } else if (e instanceof Barrel) {
                    this.detonate((Explosive)e);
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Donne le sens de direction (gauche, droite, haut, bas, etc.).
     * 
     * @param direction gauche, droite, haut, bas.
     * @return un tableau contenant la direction {x,y}
     */
    private int[] wichDirection(String direction) {
        switch(direction) {
            case "left":
                return new int[] {0, -1};
            case "right":
                return new int[] {0, 1};
            case "up":
                return new int[] {-1, 0};
            case "down":
                return new int[] {1, 0};
            case "lu":
                return new int[] {-1, -1};
            case "ru":
                return new int[] {-1, 1};
            case "ld":
                return new int[] {1, -1};
            case "rd":
                return new int[] {1, 1};
            default:
                return new int[] {0, 0};
        }
    }
    
    /**
     * Déplace un joueur vers une nouvelle position sur la grille.
     *
     * @param h Le héro à déplacer.
     * @param x Nouvelle coordonné horizontale du joueur.
     * @param y Nouvelle coordonné verticale du joueur.
     */
    private void moveHeroToNewPosition(Hero h, int x, int y) {
        this.grid[h.getX()][h.getY()] = null;
        this.grid[x][y] = h;
        h.setX(x);
        h.setY(y);
        this.fireChange();
    }
    
    /**
     * Supprime le joueur si il est mort.
     * 
     * @param h joueur a supprimer si celui-ci est mort.
     * @return true si le joueur est mort sinon false.
     */
    private boolean isHeroDead(Hero h) {
        if (!h.isAlive()) {
            for (int i=0; i < this.heroes.getSize(); i++) {
                if (this.heroes.getHero(i) == h) {
                    this.grid[h.getX()][h.getY()] = null;
                    this.fireChange();
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < this.size && y >= 0 && y < this.size;
    }

    /**
     * Vérifie si une position donnée se trouve dans la grille.
     *
     * @param x Coordonnée horizontale.
     * @param y Coordonnée verticale.
     * @return true si la position est dans la grille, false sinon.
     */
    private boolean isPosition(int x, int y) {
        return this.isValidPosition(x, y);
    }

    /**
     * Affiche l'état actuel de la grille.
     * 
     * @return l'état de la grille avec toutes les entités présentes.
     */
    @Override
    public String toString() {
        String tmp = "";
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.grid[i][j] == null) {
                    tmp += " . ";
                } else if (this.grid[i][j] instanceof Hero) {
                    tmp += " " + this.grid[i][j].getEmote();
                } else {
                    tmp += " " + this.grid[i][j].getEmote() + " ";
                }
                tmp += " ";
            }
            tmp += "\n";
        }
        return tmp;
    }
    
}