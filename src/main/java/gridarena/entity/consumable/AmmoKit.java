package gridarena.entity.consumable;

import gridarena.entity.hero.Hero;

/**
 * Représente une boite de munitions.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class AmmoKit extends Consumable {
    
    //Nombre de balles dans la boite de munitions.
    public static final int AMMOS = 5;
    
    public AmmoKit(int x, int y) {
        super(x, y, "📦️​", true, "ammo.png");
    }

    /**
     * Ajouter des munitions à un hero.
     * 
     * @param hero qui utilise la boite de munitions.
     */
    @Override
    public void useConsumable(Hero hero) {
        int ammos = hero.getAmmoRemaining()+AmmoKit.AMMOS;
        if (hero.getAmmoMax() > ammos) {
            hero.setAmmoRemaining(ammos);
        } else {
            hero.setAmmoRemaining(hero.getAmmoMax());
        }
    }

    @Override
    public String toString() {
        return super.toString() + "AmmoKit";
    }
    
}
