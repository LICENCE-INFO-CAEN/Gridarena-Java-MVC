package gridarena.model.fillgrid;

import gridarena.entity.Entity;
import gridarena.entity.hero.Hero;

/**
 * Représente le remplissage d'une grille avec les entités de base.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public interface FillStrategy {
    
    /**
     * Remplir la grille avec les entités de base (murs, kits médicaux).
     * 
     * @param grid à remplir.
     * @param walls à insérer dans la grille.
     * @param medicalKits à insérer dans la grille.
     * @param ammoKits à insérer dans la grille.
     * @param barrels à insérer dans la grille.
     */
    void fillGrid(Entity[][] grid, int walls, int medicalKits, int ammoKits, int barrels);
    
    /**
     * Remplir la grille avec un héro.
     * 
     * @param grid à remplir.
     * @param specialization du hero.
     * @return le héro inséré dans la grille.
     */
    Hero fillGridWithHero(Entity[][] grid, String specialization);
    
}
