package gridarena.controller.command;

import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;

/**
 * Commande pour placer un explosif.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class AddExplosiveCommand implements Command {
    
    private final BattlefieldModel battlefield;
    private final Hero hero;
    private final String direction;
    private final String explosiveType;

    public AddExplosiveCommand(BattlefieldModel battlefield, Hero hero, String direction, String explosiveType) {
        this.battlefield = battlefield;
        this.hero = hero;
        this.direction = direction;
        this.explosiveType = explosiveType;
    }

    @Override
    public boolean execute() {
        return this.battlefield.addExplosive(this.hero, this.direction, this.explosiveType);
    }
    
}
