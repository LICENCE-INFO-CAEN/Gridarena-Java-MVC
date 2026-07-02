package gridarena.entity.consumable;

import gridarena.entity.hero.Hero;

/**
 * Représente un kit de soins.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class MedicalKit extends Consumable {
    
    //Nombre de points de vie que contient le kit de soins.
    public static final int HEALTHS = 5;
    
    public MedicalKit(int x, int y) {
        super(x, y, "❤️​", true, "health.png");
    }

    /**
     * Ajouter des points de vie à un hero.
     * 
     * @param hero qui utilise le kit de soins.
     */
    @Override
    public void useConsumable(Hero hero) {
        int pv = hero.getHealthRemaining()+MedicalKit.HEALTHS;
        if (hero.getHealthMax() > pv) {
            hero.setHealthRemaining(pv);
        } else {
            hero.setHealthRemaining(hero.getHealthMax());
        }
    }

    @Override
    public String toString() {
        return super.toString() + "MedicalKit";
    }
    
}
