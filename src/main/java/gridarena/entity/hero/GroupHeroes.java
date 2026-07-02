package gridarena.entity.hero;

import gridarena.utils.*;

/**
 * Représente un groupe de personnes.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public interface GroupHeroes extends ListenableModel {
    
    /**
     * Obtenir la taille du groupe.
     * 
     * @return la taille du groupe.
     */
    int getSize();
    
    /**
     * Obtenir un héro du groupe avec l'indice.
     * 
     * @param index où se trouve le héro.
     * @return un héro.
     */
    Hero getHero(int index);
    
}
