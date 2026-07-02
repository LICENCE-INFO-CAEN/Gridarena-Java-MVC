package gridarena.controller.command;

import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;

/**
 * Commande pour activer le bouclier d'un héros.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class ActivateShieldCommand implements Command {
    
    private final BattlefieldModel battlefield;
    private final Hero hero;

    public ActivateShieldCommand(BattlefieldModel battlefield, Hero hero) {
        this.battlefield = battlefield;
        this.hero = hero;
    }

    @Override
    public boolean execute() {
        return this.battlefield.activateShield(this.hero);
    }
    
}
