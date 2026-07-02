package gridarena.entity.environment;

import gridarena.entity.Entity;

/**
 * Représente un mur.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class Wall extends Entity {
    
    public Wall(int x, int y) {
        super(x, y, "️█​", "wall.png", false);
    }

    @Override
    public String toString() {
        return super.toString() + "Wall";
    }
    
}
    
