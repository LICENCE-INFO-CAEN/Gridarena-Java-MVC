package gridarena.entity.hero;

/**
 * Fabrique concrète pour créer des instances du Hero par défaut.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class DefaultHeroFactory extends HeroFactory {

    @Override
    public Hero createHero(int x, int y) {
        return new DefaultHero(x, y);
    }
}
