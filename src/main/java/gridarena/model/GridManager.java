package gridarena.model;

import gridarena.entity.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Gère le stockage bidimensionnel et la géométrie spatiale de la grille de jeu.
 * Extrait de Battlefield pour respecter le Principe de Responsabilité Unique (SRP).
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class GridManager {
    
    private final Entity[][] grid;
    private final int size;

    public GridManager(int size) {
        this.grid = new Entity[size][size];
        this.size = size;
    }

    public Entity[][] getGrid() {
        return this.grid;
    }

    public int getSize() {
        return this.size;
    }

    public Entity getEntity(int x, int y) {
        if (isValidPosition(x, y)) {
            return this.grid[x][y];
        }
        return null;
    }

    public void setEntity(int x, int y, Entity entity) {
        if (isValidPosition(x, y)) {
            this.grid[x][y] = entity;
        }
    }

    public void clearCell(int x, int y) {
        if (isValidPosition(x, y)) {
            this.grid[x][y] = null;
        }
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < this.size && y >= 0 && y < this.size;
    }

    /**
     * Donne le vecteur de déplacement directionnel correspondant.
     * 
     * @param direction gauche, droite, haut, bas, etc.
     * @return un tableau contenant le vecteur de direction {x,y}
     */
    public int[] getDirectionVector(String direction) {
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
     * Recherche les entités les plus proches situés dans un rayon donné.
     * 
     * @param n Le rayon de recherche, exprimé en nombre de cases.
     * @param x Coordonné horizontale du point de recherche.
     * @param y Coordonné verticale du point de recherche.
     * @return Une liste contenant toutes les entités répondant aux critères de proximité.
     */
    public List<Entity> getNearestNeighbors(int n, int x, int y) {
        List<Entity> neighbors = new ArrayList<>();
        for (int i=0; i < this.size; i++) {
            for (int j=0; j < this.size; j++) {
                if (this.grid[i][j] != null) {
                    int diffX = Math.abs(this.grid[i][j].getX() - x);
                    int diffY = Math.abs(this.grid[i][j].getY() - y);
                    int maxi = Math.max(diffX, diffY);
                    if (maxi <= n) {
                        neighbors.add(this.grid[i][j]);
                    }
                }
            }
        }
        return neighbors;
    }
}
