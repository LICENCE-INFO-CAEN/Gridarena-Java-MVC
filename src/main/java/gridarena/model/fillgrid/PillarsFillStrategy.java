package gridarena.model.fillgrid;

import gridarena.entity.Entity;
import gridarena.entity.consumable.*;
import gridarena.entity.environment.Wall;
import gridarena.entity.explosive.Barrel;
import gridarena.entity.hero.Hero;
import gridarena.entity.hero.HeroFactory;

import java.util.Random;

/**
 * Représente la stratégie avec des piliers de murs réguliers (style Bomberman).
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class PillarsFillStrategy implements FillStrategy {

    @Override
    public void fillGrid(Entity[][] grid, int walls, int medicalKits, int ammoKits, int barrels) {
        int size = grid.length;

        // Placer les piliers à intervalles réguliers de 2 cases (évite les bords immédiats)
        for (int i = 2; i < size - 2; i += 2) {
            for (int j = 2; j < size - 2; j += 2) {
                grid[i][j] = new Wall(i, j);
            }
        }

        // Placer un baril explosif au centre exact si possible
        int center = size / 2;
        if (grid[center][center] == null) {
            grid[center][center] = new Barrel(center, center);
        }

        // Positions symétriques pour les kits de soin et kits de munitions
        int[][] itemPositions = {
            {1, 1}, {1, size - 2}, {size - 2, 1}, {size - 2, size - 2},
            {center, 1}, {center, size - 2}, {1, center}, {size - 2, center}
        };

        for (int k = 0; k < itemPositions.length; k++) {
            int r = itemPositions[k][0];
            int c = itemPositions[k][1];
            if (grid[r][c] == null) {
                if (k < 4) {
                    grid[r][c] = new MedicalKit(r, c);
                } else {
                    grid[r][c] = new AmmoKit(r, c);
                }
            }
        }
    }

    @Override
    public Hero fillGridWithHero(Entity[][] grid, HeroFactory factory) {
        Random random = new Random();
        int size = grid.length;
        int x = random.nextInt(grid.length);
        int y = random.nextInt(grid[0].length);
        while (true) {
            if (grid[x][y] == null && (x != 0 && y != 0) && (x != 0 && y != size - 1) 
                && (x != size - 1 && y != 0) && (x != size - 1 && y != size - 1)) {
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
