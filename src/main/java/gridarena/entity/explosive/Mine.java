package gridarena.entity.explosive;

import gridarena.entity.hero.Hero;

/**
 * Représente une mine.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class Mine extends Explosive {
    
    //Rayon d'explosion d'une bombe.
    public static final int EXPLOSION_RADIUS = 1;
    //Dommages causés par l'explosion de la bombe.
    public static final int DAMAGES = 10;
    
    public Mine(int x, int y, Hero belongsTo) {
        super(x, y, belongsTo, "☢️​", Mine.EXPLOSION_RADIUS, Mine.DAMAGES, true, "mine.png");
    }

    @Override
    public String toString() {
        return super.toString() + "Mine";
    }
    
}
