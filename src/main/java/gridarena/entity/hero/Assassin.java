package gridarena.entity.hero;

/**
 * Représente la spécialisation Assassin du Hero.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Assassin extends Hero {
    
    /**
     * Constructeur d'un Assassin.
     * 
     * @param x position horizontale
     * @param y position verticale
     */
    public Assassin(int x, int y) {
        super("assassin", 40, 50, 1, 6, 6, x, y, "blue.png");
    }
}
