package gridarena.entity.hero;

/**
 * Fabrique concrète pour créer des instances de Mastodonte.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class MastodonteFactory extends HeroFactory {

    @Override
    public Hero createHero(int x, int y) {
        return new Mastodonte(x, y);
    }
}
