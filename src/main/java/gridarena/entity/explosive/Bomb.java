package gridarena.entity.explosive;

import gridarena.entity.EntityVisitor;
import gridarena.entity.hero.Hero;

/**
 * Représente une bombe.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class Bomb extends Explosive {
    
    //Délai d'explosion d'une bombe.
    public static final int EXPLOSION_DELAY = 3;
    //Rayon d'explosion d'une bombe.
    public static final int EXPLOSION_RADIUS = 1;
    //Dommages causés par l'explosion de la bombe.
    public static final int DAMAGES = 10;
    //Délai d'explosion d'une bombe.
    private int explosionDelay;
    
    public Bomb(int x, int y, Hero belongsTo) {
        super(x, y, belongsTo, "💣", Bomb.EXPLOSION_RADIUS, Bomb.DAMAGES, false, "bomb.png");
        this.explosionDelay = Bomb.EXPLOSION_DELAY;
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }

    public int getExplosionDelay() {
        return this.explosionDelay;
    }

    /**
     * Décrémentation du délai d'explosion.
     * 
     * @return true si la bombe peut exploser sinon false.
     */
    public boolean decrementeTimer() {
        if (this.explosionDelay > 0) this.explosionDelay --;
        return this.explosionDelay == 0;
    }
    
    @Override
    public String toString() {
        return super.toString() + "Bomb";
    }
    
}
