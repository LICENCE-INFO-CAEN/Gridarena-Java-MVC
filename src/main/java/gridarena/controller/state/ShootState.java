package gridarena.controller.state;

import gridarena.controller.command.Command;
import gridarena.controller.command.ShootCommand;
import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;

/**
 * État du contrôleur pour faire tirer le héros.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class ShootState implements ControllerState {

    @Override
    public Command getCommandForDirection(BattlefieldModel battlefield, Hero hero, String direction) {
        return new ShootCommand(battlefield, hero, direction);
    }

    @Override
    public String getName() {
        return "Tirer";
    }
}
