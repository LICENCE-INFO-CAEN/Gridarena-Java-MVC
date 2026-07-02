package gridarena.controller.state;

import gridarena.controller.command.Command;
import gridarena.controller.command.ActivateShieldCommand;
import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;

/**
 * État du contrôleur pour activer le bouclier.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class ShieldState implements ControllerState {

    @Override
    public Command getCommandForDirection(BattlefieldModel battlefield, Hero hero, String direction) {
        return new ActivateShieldCommand(battlefield, hero);
    }

    @Override
    public String getName() {
        return "Bouclier";
    }
}
