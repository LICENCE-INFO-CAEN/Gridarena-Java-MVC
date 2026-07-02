package gridarena.entity.hero;

/**
 * Fabrique concrète pour créer des instances d'Assassin.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class AssassinFactory extends HeroFactory {

    @Override
    public Hero createHero(int x, int y) {
        return new Assassin(x, y);
    }
}
