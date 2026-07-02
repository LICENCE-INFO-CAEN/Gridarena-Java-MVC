package gridarena.entity.hero;

/**
 * Représente un groupe de héros éditable.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public interface GroupHeroesEditable extends GroupHeroes {
    
    /**
     * Ajouter un hero dans le groupe.
     * 
     * @param h hero a ajouté.
     */
    void addHero(Hero h);
    
}
