package gridarena.entity.hero;

/**
 * Fabrique concrète pour créer des instances de Warrior.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class WarriorFactory extends HeroFactory {

    @Override
    public Hero createHero(int x, int y) {
        return new Warrior(x, y);
    }
}
