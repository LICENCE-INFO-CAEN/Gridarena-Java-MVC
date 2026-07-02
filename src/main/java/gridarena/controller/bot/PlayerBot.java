package gridarena.controller.bot;

import gridarena.controller.GameController;
import gridarena.model.BattlefieldProxy;
import gridarena.view.Player;

/**
 * Représente un joueur robot.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class PlayerBot implements Player {

    private GameController gameController;
    private BattlefieldProxy battlefieldProxy;
    private String name;
    private BotStrategy botStrategy;

    public PlayerBot(GameController gameController, BattlefieldProxy battlefieldProxy, String name, BotStrategy botStrategy) {
        this.gameController = gameController;
        this.battlefieldProxy = battlefieldProxy;
        this.name = name;
        this.botStrategy = botStrategy;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void takeMyTurn() {
        try {
            Thread.sleep(1000);
            this.botStrategy.actionStrategy(this.battlefieldProxy);
            synchronized (this.gameController) {
                this.gameController.notifyAll();
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException("Error thread sleep : "+ex.getMessage());
        }
    }

}