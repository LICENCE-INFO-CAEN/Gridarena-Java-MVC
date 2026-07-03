package gridarena.model.fillgrid;

import gridarena.entity.Entity;
import gridarena.entity.consumable.*;
import gridarena.entity.environment.Wall;
import gridarena.entity.explosive.Barrel;
import gridarena.entity.hero.Hero;
import gridarena.entity.hero.HeroFactory;

import java.util.Random;

/**
 * Représente la stratégie avec un anneau de murs carré concentrique doté de portes.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class RingsFillStrategy implements FillStrategy {

    @Override
    public void fillGrid(Entity[][] grid, int walls, int medicalKits, int ammoKits, int barrels) {
        int size = grid.length;
        int center = size / 2;

        // L'anneau se situe à environ un quart de la distance par rapport aux bords
        int dist = size / 4;
        if (dist < 1) dist = 1;

        int min = dist;
        int max = size - 1 - dist;

        // Dessiner le carré de murs sauf au milieu de chaque segment pour laisser des passages (portes)
        for (int i = min; i <= max; i++) {
            if (i != center) {
                grid[min][i] = new Wall(min, i); // Mur du haut
                grid[max][i] = new Wall(max, i); // Mur du bas
                grid[i][min] = new Wall(i, min); // Mur de gauche
                grid[i][max] = new Wall(i, max); // Mur de droite
            }
        }

        // Placer un baril explosif au centre de la structure
        if (grid[center][center] == null) {
            grid[center][center] = new Barrel(center, center);
        }

        // Placer des consommables aux coins de la carte et à l'intérieur de l'anneau
        int[][] itemPositions = {
            {0, 0}, {0, size - 1}, {size - 1, 0}, {size - 1, size - 1},
            {center - 1, center - 1}, {center - 1, center + 1},
            {center + 1, center - 1}, {center + 1, center + 1}
        };

        for (int k = 0; k < itemPositions.length; k++) {
            int r = itemPositions[k][0];
            int c = itemPositions[k][1];
            if (r >= 0 && r < size && c >= 0 && c < size && grid[r][c] == null) {
                if (k % 2 == 0) {
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
