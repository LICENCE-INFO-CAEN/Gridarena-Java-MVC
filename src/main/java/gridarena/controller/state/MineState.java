package gridarena.controller.state;

import gridarena.controller.command.Command;
import gridarena.controller.command.AddExplosiveCommand;
import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;

/**
 * État du contrôleur pour poser une mine.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class MineState implements ControllerState {

    @Override
    public Command getCommandForDirection(BattlefieldModel battlefield, Hero hero, String direction) {
        return new AddExplosiveCommand(battlefield, hero, direction, "mine");
    }

    @Override
    public String getName() {
        return "Mine";
    }
}
