package gridarena.controller.state;

import gridarena.controller.command.Command;
import gridarena.controller.command.AxAttackCommand;
import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;

/**
 * État du contrôleur pour effectuer une attaque à la hache.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class AxAttackState implements ControllerState {

    @Override
    public Command getCommandForDirection(BattlefieldModel battlefield, Hero hero, String direction) {
        return new AxAttackCommand(battlefield, hero, direction);
    }

    @Override
    public String getName() {
        return "Hache";
    }
}
