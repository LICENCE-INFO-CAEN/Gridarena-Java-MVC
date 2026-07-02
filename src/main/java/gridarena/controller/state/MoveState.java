package gridarena.controller.state;

import gridarena.controller.command.Command;
import gridarena.controller.command.MoveCommand;
import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;

/**
 * État du contrôleur pour le déplacement du héros.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class MoveState implements ControllerState {

    @Override
    public Command getCommandForDirection(BattlefieldModel battlefield, Hero hero, String direction) {
        return new MoveCommand(battlefield, hero, direction);
    }

    @Override
    public String getName() {
        return "Bouger";
    }
}
