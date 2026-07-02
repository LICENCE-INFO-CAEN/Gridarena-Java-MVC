package gridarena.entity.hero;

/**
 * Représente un héros personnalisé avec des statistiques définies à la création.
 *
 * @author Florian Pépin
 * @version 1.0
 */
public class CustomHero extends Hero {
    
    /**
     * Constructeur d'un CustomHero.
     */
    public CustomHero(int healthMax, int ammoMax, int shieldMax, int mineMax, int bombMax, int x, int y) {
        super("custom", healthMax, ammoMax, shieldMax, mineMax, bombMax, x, y, "violet.png");
    }
}
