package gridarena.entity.consumable;

import gridarena.entity.Entity;
import gridarena.entity.hero.Hero;

/**
 * Représente un consommable.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public abstract class Consumable extends Entity {

    public Consumable(int x, int y, String emote, boolean walkable, String image) {
        super(x, y, emote, image, walkable);
    }

    /**
     * Utiliser l'objet consommable.
     * 
     * @param hero qui utilise le consommable.
     */
    public abstract void useConsumable(Hero hero);

    @Override
    public String toString() {
        return super.toString() + "Consumable -> ";
    }

}