package gridarena.entity.hero;

/**
 * Représente la spécialisation Warrior du Hero.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Warrior extends Hero {
    
    /**
     * Constructeur d'un Warrior.
     * 
     * @param x position horizontale
     * @param y position verticale
     */
    public Warrior(int x, int y) {
        super("warrior", 50, 50, 3, 3, 3, x, y, "vert.png");
    }
}
