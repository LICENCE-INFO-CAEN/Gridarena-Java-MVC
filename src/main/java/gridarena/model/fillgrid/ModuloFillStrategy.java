package gridarena.model.fillgrid;

import gridarena.entity.Entity;
import gridarena.entity.consumable.*;
import gridarena.entity.environment.Wall;
import gridarena.entity.explosive.Barrel;
import gridarena.entity.hero.Hero;
import gridarena.entity.hero.HeroFactory;

import java.util.*;

/**
 * Représente la stratégie avec modulo.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class ModuloFillStrategy implements FillStrategy {
    
    @Override
    public void fillGrid(Entity[][] grid, int walls, int medicalKits, int ammoKits, int barrels) {
        Random random = new Random();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int choice = random.nextInt(500);
                int placement = random.nextInt(500);
                if (choice % 2 == 0) {
                    if (i % 2 == 0 && j % 2 == 0) {
                        if (placement % 2 == 0 && medicalKits > 0) {
                            grid[i][j] = new MedicalKit(i, j);
                            medicalKits--;
                        } else if (ammoKits > 0) {
                            grid[i][j] = new AmmoKit(i, j);
                            ammoKits--; 
                        }
                    } else {
                        if (placement % 2 == 0 && walls > 0) {
                            grid[i][j] = new Wall(i, j);
                            walls--; 
                        } else if (barrels > 0){
                            grid[i][j] = new Barrel(i, j);
                            barrels--; 
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public Hero fillGridWithHero(Entity[][] grid, String specialization) {
        Random random = new Random();
        int size = grid.length;
        int x = random.nextInt(grid.length);
        int y = random.nextInt(grid[0].length);
        while(true) {
            if (grid[x][y] == null && (x!=0 && y!=0) && (x!=0 && y!=size-1) && (x!=size-1 && y!=0) && (x!=size-1 && y!=size-1)) {
                Hero hero = HeroFactory.getFactory(specialization).createHero(x, y);
                grid[x][y] = hero;
                return hero;
            } else {
                x = random.nextInt(grid.length);
                y = random.nextInt(grid[0].length);
            }
        }
    }
    
}
