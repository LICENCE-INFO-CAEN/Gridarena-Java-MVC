package gridarena.controller.command;

import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;

/**
 * Commande pour effectuer une attaque à la hache.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class AxAttackCommand implements Command {
    
    private final BattlefieldModel battlefield;
    private final Hero hero;
    private final String direction;

    public AxAttackCommand(BattlefieldModel battlefield, Hero hero, String direction) {
        this.battlefield = battlefield;
        this.hero = hero;
        this.direction = direction;
    }

    @Override
    public boolean execute() {
        return this.battlefield.axAttack(this.hero, this.direction);
    }
    
}
