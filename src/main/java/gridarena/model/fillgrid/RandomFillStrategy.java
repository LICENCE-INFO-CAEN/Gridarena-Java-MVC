package gridarena.model.fillgrid;

import gridarena.entity.Entity;
import gridarena.entity.consumable.*;
import gridarena.entity.environment.Wall;
import gridarena.entity.explosive.Barrel;
import gridarena.entity.hero.Hero;
import gridarena.entity.hero.HeroFactory;

import java.util.*;

/**
 * Représente une stratégie avec des placements aléatoires.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class RandomFillStrategy implements FillStrategy {
    
    @Override
    public void fillGrid(Entity[][] grid, int walls, int medicalKits, int ammoKits, int barrels) {
        Random random = new Random();
        int i = random.nextInt(grid.length);
        int j = random.nextInt(grid.length);
        while(walls > 0 || medicalKits > 0 || ammoKits > 0 || barrels > 0) {
            if (grid[i][j] == null) {
                if (walls > 0 && i != 0 && j != 0) {
                    grid[i][j] = new Wall(i, j);
                    walls --;   
                } else if (medicalKits > 0) {
                    grid[i][j] = new MedicalKit(i, j);
                    medicalKits --;  
                } else if (ammoKits > 0) {
                    grid[i][j] = new AmmoKit(i, j);
                    ammoKits --; 
                } else {
                    grid[i][j] = new Barrel(i, j);
                    barrels --; 
                }
            }
            i = random.nextInt(grid.length);
            j = random.nextInt(grid.length);
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
