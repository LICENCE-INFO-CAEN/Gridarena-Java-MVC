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
 * Représente un champ de bataille (Modèle).
 * Gère uniquement la logique métier globale et les règles du combat.
 * Délégué la gestion géométrique et spatiale à un GridManager.
 * Utilise des classes de visiteurs dédiées pour implémenter ses règles.
 *
 * @author Florian Pépin.
 * @version 4.0
 */
public class Battlefield extends AbstractListenableModel implements BattlefieldModel {
    
    private final FillStrategy fillStrategy;
    private final GroupHeroesArrayList heroes;
    private final List<Bomb> bombs;
    private final GridManager gridManager;
    private Hero currentHero;
    
    public Battlefield(int size, int walls, int medicalKits, int ammoKits, int barrels, FillStrategy fillStrategy) {
        super();
        this.fillStrategy = fillStrategy;
        this.currentHero = null;
        this.heroes = new GroupHeroesArrayList();
        this.bombs = new ArrayList<>();
        this.gridManager = new GridManager(size);
        this.fillStrategy.fillGrid(this.gridManager.getGrid(), walls, medicalKits, ammoKits, barrels);
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
        return this.gridManager.getGrid();
    }
    
    @Override
    public int getSize() {
        return this.gridManager.getSize();
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
        Hero hero = this.fillStrategy.fillGridWithHero(this.gridManager.getGrid(), factory);
        this.heroes.addHero(hero);
        this.fireChange();
        return hero;
    }

    @Override
    public boolean addExplosive(Hero h, String direction, String explosiveType) {
        int[] pos = this.gridManager.getDirectionVector(direction);
        int x = h.getX() + pos[0];
        int y = h.getY() + pos[1];
        if (this.gridManager.isValidPosition(x, y)) {
            Entity e = this.gridManager.getEntity(x, y);
            if (e == null) {
                Explosive explosive = null;
                if ("mine".equals(explosiveType) && h.useMine()) {
                    explosive = new Mine(x, y, h);
                } else if ("bomb".equals(explosiveType) && h.useBomb()) {
                    Bomb bomb = new Bomb(x, y, h);
                    this.bombs.add(bomb);
                    explosive = bomb;
                }

                if (explosive == null) {
                    return false;
                } else {
                    this.gridManager.setEntity(x, y, explosive);
                    this.fireChange();
                    return true;
                }
            } else {
                AddExplosiveVisitor visitor = new AddExplosiveVisitor(this);
                e.accept(visitor);
                if (visitor.isExplosive()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean moveHero(Hero h, String direction) {
        int[] pos = this.gridManager.getDirectionVector(direction);
        int x = h.getX() + pos[0];
        int y = h.getY() + pos[1];
        if (this.gridManager.isValidPosition(x, y)) {
            Entity e = this.gridManager.getEntity(x, y);
            if (e == null) {
                this.moveHeroToNewPosition(h, x, y);
                return true;
            } else {
                MoveVisitor visitor = new MoveVisitor(h, x, y, this);
                e.accept(visitor);
                if (visitor.hasMoved()) {
                    return true;
                }
            }
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
    
    public void decrementBombs() {
        for (int i = 0; i < this.bombs.size(); i++) {
            boolean state = this.bombs.get(i).decrementeTimer();
            if (state) {
                this.detonate(this.bombs.get(i));
                this.bombs.remove(i);
            }
        }
    }

    void detonate(Explosive explosive) {
        List<Entity> entities = this.gridManager.getNearestNeighbors(explosive.getExplosionRadius(), explosive.getX(), explosive.getY());
        this.gridManager.clearCell(explosive.getX(), explosive.getY());
        DetonateVisitor visitor = new DetonateVisitor(explosive, this);
        for (Entity entitie : entities) {
            entitie.accept(visitor);
        }
        this.fireChange();
    }
    
    @Override
    public boolean axAttack(Hero h, String d) {
        int[] direction = this.gridManager.getDirectionVector(d);
        int x = h.getX() + direction[0];
        int y = h.getY() + direction[1];
        if (this.gridManager.isValidPosition(x, y)) {
            Entity e = this.gridManager.getEntity(x, y);
            if (e != null) {
                AxAttackVisitor visitor = new AxAttackVisitor(h, this);
                e.accept(visitor);
                return visitor.hasHit();
            }
        }
        return false;
    }
    
    @Override
    public boolean shootHero(Hero h, String d) {
        h.setAmmoRemaining(h.getAmmoRemaining()-1);
        int[] direction = this.gridManager.getDirectionVector(d);
        int x = h.getX();
        int y = h.getY();
        for (int i = 0; i < this.gridManager.getSize(); i++) {
            x += direction[0];
            y += direction[1];
            if (this.gridManager.isValidPosition(x, y)) {
                Entity e = this.gridManager.getEntity(x, y);
                if (e != null) {
                    ShootHeroVisitor visitor = new ShootHeroVisitor(h, this);
                    e.accept(visitor);
                    if (visitor.getHitObstacle() != null) {
                        return visitor.getHitObstacle();
                    }
                }
            }
        }
        return false;
    }
    
    void moveHeroToNewPosition(Hero h, int x, int y) {
        this.gridManager.clearCell(h.getX(), h.getY());
        this.gridManager.setEntity(x, y, h);
        h.setX(x);
        h.setY(y);
        this.fireChange();
    }
    
    boolean isHeroDead(Hero h) {
        if (!h.isAlive()) {
            for (int i = 0; i < this.heroes.getSize(); i++) {
                if (this.heroes.getHero(i) == h) {
                    this.gridManager.clearCell(h.getX(), h.getY());
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
        return this.gridManager.isValidPosition(x, y);
    }

    @Override
    public String toString() {
        String tmp = "";
        int size = this.gridManager.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Entity e = this.gridManager.getEntity(i, j);
                if (e == null) {
                    tmp += " . ";
                } else {
                    ToStringVisitor visitor = new ToStringVisitor();
                    e.accept(visitor);
                    tmp += visitor.getFormatted();
                }
                tmp += " ";
            }
            tmp += "\n";
        }
        return tmp;
    }
    void notifyChange() {
        this.fireChange();
    }
    
}