package gridarena.model;

import gridarena.entity.Entity;
import gridarena.entity.explosive.Mine;
import gridarena.entity.hero.*;
import gridarena.utils.AbstractListenableModel;
import gridarena.utils.ModelListener;

/**
 * Représente le proxy permettant de filtrer la grille d'un joueur.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class BattlefieldProxy extends AbstractListenableModel implements BattlefieldModel, ModelListener {

    private Battlefield battlefield;
    private Hero hero;
    
    public BattlefieldProxy(Battlefield battlefield) {
        super();
        this.battlefield = battlefield;
        this.battlefield.addModelListener(this);
        this.hero = null;
    }

    @Override
    public Hero getCurrentHero() {
        return this.hero;
    }

    @Override
    public void setCurrentHero(int i) {
        this.battlefield.setCurrentHero(i);
    }
    
    public Entity[][] filteredGrid(Hero hero, Entity[][] grid) {
        Entity[][] originalGrid = grid;
        int size = grid.length;
        grid = new Entity[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (originalGrid[i][j] instanceof Mine) {
                    Mine e = (Mine) originalGrid[i][j];
                    if (e.getBelongsTo() == hero) {
                        grid[i][j] = e;
                    } else {
                        grid[i][j] = null;
                    }
                } else {
                    grid[i][j] = originalGrid[i][j];
                }
            }
        }
        return grid;
    }
    
    @Override
    public GroupHeroesArrayList getHeroes() {
        return this.battlefield.getHeroes();
    }
    
    @Override
    public Hero addHero(HeroFactory factory) {
        this.hero = this.battlefield.addHero(factory);
        return this.hero;
    }

    @Override
    public Entity[][] getGrid() {
        return this.filteredGrid(this.hero, this.battlefield.getGrid());
    }

    @Override
    public int getSize() {
        return this.battlefield.getSize();
    }

    @Override
    public boolean addExplosive(Hero hero, String direction, String explosive) {
        if (this.hero == hero) {
            return this.battlefield.addExplosive(hero, direction, explosive);
        }
        return false;
    }

    @Override
    public boolean moveHero(Hero hero, String string) {
        if (this.hero == hero) {
            return this.battlefield.moveHero(hero, string);
        }
        return false;
    }

    @Override
    public boolean activateShield(Hero hero) {
        if (this.hero == hero) {
            return this.battlefield.activateShield(hero);
        }
        return false;
    }

    @Override
    public boolean shootHero(Hero hero, String d) {
        if (this.hero == hero) {
            return this.battlefield.shootHero(hero, d);
        }
        return false;
    }
    
    @Override
    public boolean axAttack(Hero h, String d) {
        if (this.hero == h) {
            return this.battlefield.axAttack(h, d);
        }
        return false;
    }

    @Override
    public boolean isValidPosition(int x, int y) {
        return this.battlefield.isValidPosition(x, y);
    }
    
    @Override
    public void updatedModel(Object source) {
        this.fireChange();
    }
    
    @Override
    public String toString() {
        String tmp = "";
        Entity[][] map = this.battlefield.getGrid();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == null) {
                    tmp += " . ";
                } else if (map[i][j] instanceof Hero) {
                    tmp += " " + map[i][j].getEmote()+" ";   
                } else if(map[i][j] instanceof Mine) {
                    Mine e = (Mine) map[i][j];
                    if (e.getBelongsTo() != this.hero) {
                        tmp += " . ";
                    }
                } else {
                    tmp += " " + map[i][j].getEmote() + " ";
                }
                tmp += " ";
            }
            tmp += "\n";
        }
        return tmp;
    }
    
}
