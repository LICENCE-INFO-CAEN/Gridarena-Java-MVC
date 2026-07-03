package gridarena.controller;

import gridarena.controller.bot.*;
import gridarena.model.GameConfig;
import gridarena.model.GameConfigValidator;
import gridarena.model.fillgrid.*;
import gridarena.view.gui.AdminPanel;

import javax.swing.Timer;

/**
 * Contrôleur associé au panneau d'administration (AdminPanel).
 * Gère le cycle de démarrage, de rafraîchissement et d'arrêt de la partie.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class AdminController {

    private final AdminPanel view;
    private GameController gameController;
    private Timer refreshTimer;

    public AdminController(AdminPanel view) {
        this.view = view;
    }

    /**
     * Valide la configuration, résout les stratégies et lance le thread de jeu.
     *
     * @param config les paramètres de la partie
     */
    public void handleStart(GameConfig config) {
        try {
            GameConfigValidator.validate(config);
        } catch (IllegalArgumentException ex) {
            view.showError("Configuration Invalide", ex.getMessage());
            return;
        }

        FillStrategy fillStrategy = resolveFillStrategy(config.getFillStrategy());
        BotStrategy botStrategy = resolveBotStrategy(config.getBotStrategy());

        gameController = new GameController(
                false,
                config.getGuiPlayers(),
                config.getCliPlayers(),
                config.getBotPlayers(),
                config.getGridSize(),
                config.getWalls(),
                config.getMedKits(),
                config.getAmmoKits(),
                config.getBarrels(),
                fillStrategy
        );

        view.displayRunningGame(gameController.getBattlefield());
        view.setControlState(true);

        refreshTimer = new Timer(200, evt -> view.refreshStatsTable());
        refreshTimer.start();

        gameController.setOnGameFinished(winnerName -> {
            if (refreshTimer != null) {
                refreshTimer.stop();
            }
            view.setControlState(false);
            view.displayWaitingScreen();
            if (winnerName != null) {
                view.showInfo("Fin de partie", "La partie est terminée !\nLe gagnant est : " + winnerName);
            } else {
                view.showInfo("Fin de partie", "La partie est terminée !\nÉgalité (aucun survivant).");
            }
        });

        gameController.demarrer(botStrategy);
    }

    /**
     * Coupe la partie en cours et réinitialise l'affichage.
     */
    public void handleStop() {
        if (gameController != null) {
            gameController.stop();
        }
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
        view.displayWaitingScreen();
        view.setControlState(false);
    }

    private FillStrategy resolveFillStrategy(String name) {
        if ("Pattern - Pillars".equalsIgnoreCase(name)) {
            return new PillarsFillStrategy();
        } else if ("Pattern - Rings".equalsIgnoreCase(name)) {
            return new RingsFillStrategy();
        }
        return new CrossFillStrategy();
    }

    private BotStrategy resolveBotStrategy(String name) {
        if (name != null) {
            if (name.contains("Dijkstra")) {
                return new DijkstraBotStrategy();
            } else if (name.contains("Greedy")) {
                return new GreedyBotStrategy();
            }
        }
        return new AStarBotStrategy();
    }
}
