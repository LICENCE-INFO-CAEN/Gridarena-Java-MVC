package gridarena.controller.command;

import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;

/**
 * Commande pour déplacer un héros.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class MoveCommand implements Command {
    
    private final BattlefieldModel battlefield;
    private final Hero hero;
    private final String direction;

    public MoveCommand(BattlefieldModel battlefield, Hero hero, String direction) {
        this.battlefield = battlefield;
        this.hero = hero;
        this.direction = direction;
    }

    @Override
    public boolean execute() {
        return this.battlefield.moveHero(this.hero, this.direction);
    }
    
}
