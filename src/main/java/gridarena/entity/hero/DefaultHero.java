package gridarena.entity.hero;

/**
 * Représente la spécialisation par défaut (Warrior avec statistiques modifiées) du Hero.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class DefaultHero extends Hero {
    
    /**
     * Constructeur d'un Hero par défaut.
     * 
     * @param x position horizontale
     * @param y position verticale
     */
    public DefaultHero(int x, int y) {
        super("warrior", 50, 100, 3, 3, 3, x, y, "vert.png");
    }
}
