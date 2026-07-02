package gridarena.controller.command;

import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;

/**
 * Commande pour faire tirer un héros.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class ShootCommand implements Command {
    
    private final BattlefieldModel battlefield;
    private final Hero hero;
    private final String direction;

    public ShootCommand(BattlefieldModel battlefield, Hero hero, String direction) {
        this.battlefield = battlefield;
        this.hero = hero;
        this.direction = direction;
    }

    @Override
    public boolean execute() {
        return this.battlefield.shootHero(this.hero, this.direction);
    }
    
}
