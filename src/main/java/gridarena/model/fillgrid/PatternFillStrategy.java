package gridarena.model.fillgrid;

import gridarena.entity.Entity;
import gridarena.entity.consumable.*;
import gridarena.entity.environment.Wall;
import gridarena.entity.explosive.Barrel;
import gridarena.entity.hero.Hero;
import gridarena.entity.hero.HeroFactory;

import java.util.*;

/**
 * Représente la stratégie avec un pattern prédéfini.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class PatternFillStrategy implements FillStrategy {
    
    @Override
    public void fillGrid(Entity[][] grid, int walls, int medicalKits, int ammoKits, int barrels) {
        int size = grid.length;
        int center = size/2;
        int[][] posMedAmmo = {{0,0},{0, size-1},{size-1, 0},{size-1,size-1},{0,1},{0, size-2},{size-1, 1},{size-1,size-2}};
        grid[center][center] = new Barrel(center, center);
        for (int i = 0; i < size; i++) {
            if (i != center && i != center-1 && i != center+1) {
                grid[center][i] = new Wall(center, i);
            }
        }
        for (int j = 0; j < size; j++) {
            if (j != center && j != center-1 && j != center+1) {
                grid[j][center] = new Wall(j, center);
            }
        }
        for (int k = 0; k < posMedAmmo.length; k++) {
            if (k < 4) {
                grid[posMedAmmo[k][0]][posMedAmmo[k][1]] = new MedicalKit(posMedAmmo[k][0], posMedAmmo[k][1]);
            } else {
                grid[posMedAmmo[k][0]][posMedAmmo[k][1]] = new AmmoKit(posMedAmmo[k][0], posMedAmmo[k][1]);   
            }
        }
    }

    @Override
    public Hero fillGridWithHero(Entity[][] grid, HeroFactory factory) {
        Random random = new Random();
        int x = random.nextInt(grid.length);
        int y = random.nextInt(grid[0].length);
        while(true) {
            if (grid[x][y] == null) {
                Hero hero = factory.createHero(x, y);
                grid[x][y] = hero;
                return hero;
            } else {
                x = random.nextInt(grid.length);
                y = random.nextInt(grid[0].length);
            }
        }
    }
    
}
