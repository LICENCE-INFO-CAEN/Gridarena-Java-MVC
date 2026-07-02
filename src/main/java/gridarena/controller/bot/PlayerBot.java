package gridarena.controller.bot;

import gridarena.controller.GameController;
import gridarena.model.BattlefieldModel;
import gridarena.view.Player;

/**
 * Représente un joueur robot.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class PlayerBot implements Player {

    private GameController gameController;
    private BattlefieldModel battlefieldProxy;
    private String name;
    private BotStrategy botStrategy;

    public PlayerBot(GameController gameController, BattlefieldModel battlefieldProxy, String name, BotStrategy botStrategy) {
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
        } catch (InterruptedException ex) {
            // Partie arrêtée pendant le sleep du bot
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void showLeaderboard() {
        // Les bots n'ont pas d'interface graphique ou textuelle pour afficher le classement.
    }

    @Override
    public boolean hasHero() {
        return this.battlefieldProxy.getCurrentHero() != null;
    }

}