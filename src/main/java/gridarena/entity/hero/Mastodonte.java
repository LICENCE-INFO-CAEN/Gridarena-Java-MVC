package gridarena.entity.hero;

/**
 * Représente la spécialisation Mastodonte du Hero.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Mastodonte extends Hero {
    
    /**
     * Constructeur d'un Mastodonte.
     * 
     * @param x position horizontale
     * @param y position verticale
     */
    public Mastodonte(int x, int y) {
        super("mastodonte", 60, 50, 6, 1, 1, x, y, "violet.png");
    }
}
