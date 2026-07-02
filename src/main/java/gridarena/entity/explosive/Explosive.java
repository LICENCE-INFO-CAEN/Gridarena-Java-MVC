package gridarena.entity.explosive;

import gridarena.entity.Entity;
import gridarena.entity.hero.Hero;

/**
 * Représente un explosif.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public abstract class Explosive extends Entity {
    
    //propriétaire de l'explosif.
    protected Hero belongsTo;
    //rayon d'explosion.
    protected int explosionRadius;
    //dommages causés par l'explosion.
    protected int damages;

    public Explosive(int x, int y, Hero belongsTo, String emote, int explosionRadius, int damages, boolean walkable, String image) {
        super(x, y, emote,image, walkable);
        this.belongsTo = belongsTo;
        this.explosionRadius = explosionRadius;
        this.damages = damages;
    }

    public int getExplosionRadius() {
        return this.explosionRadius;
    }

    public int getDamages() {
        return this.damages;
    }

    public Hero getBelongsTo() {
        return this.belongsTo;
    }

    /**
     * Déclencher l'explosion.
     * 
     * @param hero qui prend des dégats.
     * @return true si l'explosif peut exploser.
     */
    public boolean explode(Hero hero) {
        if (!hero.isImmune()) {
            hero.setHealthRemaining(hero.getHealthRemaining()-this.damages);
        }
        return true;
    }

    @Override
    public String toString() {
        return "Explosive : {" + "explosionRadius=" + explosionRadius + ", damage=" + damages + "} -> ";
    }
    
}
