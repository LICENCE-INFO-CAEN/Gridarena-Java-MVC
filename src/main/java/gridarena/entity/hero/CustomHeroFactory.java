package gridarena.entity.hero;

/**
 * Fabrique pour créer des instances de héros personnalisés.
 *
 * @author Florian Pépin
 * @version 1.0
 */
public class CustomHeroFactory extends HeroFactory {

    private final int healthMax;
    private final int ammoMax;
    private final int shieldMax;
    private final int mineMax;
    private final int bombMax;

    public CustomHeroFactory(int healthMax, int ammoMax, int shieldMax, int mineMax, int bombMax) {
        this.healthMax = healthMax;
        this.ammoMax = ammoMax;
        this.shieldMax = shieldMax;
        this.mineMax = mineMax;
        this.bombMax = bombMax;
    }

    @Override
    public Hero createHero(int x, int y) {
        return new CustomHero(this.healthMax, this.ammoMax, this.shieldMax, this.mineMax, this.bombMax, x, y);
    }
}
