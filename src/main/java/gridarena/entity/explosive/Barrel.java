package gridarena.entity.explosive;

/**
 * Représente un barrel explosif.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class Barrel extends Explosive {
   
    //Rayon d'explosion d'un barril.
    public static final int EXPLOSION_RADIUS = 1;
    //Dommages causés par l'explosion du barril.
    public static final int DAMAGES = 10;
    
    public Barrel(int x, int y) {
        super(x, y, null, "⛽", Barrel.EXPLOSION_RADIUS, Barrel.DAMAGES, false, "barrel.png");
    }
    
    @Override
    public String toString() {
        return super.toString() + "Barrel";
    }
    
}
