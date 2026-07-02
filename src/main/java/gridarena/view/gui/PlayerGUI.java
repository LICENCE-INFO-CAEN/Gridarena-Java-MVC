package gridarena.view.gui;

import gridarena.controller.GameController;
import gridarena.model.BattlefieldModel;
import gridarena.view.Player;

import java.awt.Color;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;

/**
 * Représente l'interface utilisateur et le contrôleur de tour d'un joueur graphique.
 * Déploie et délègue l'affichage à un composant PlayerFrame dédié.
 *
 * Synchronisation :
 *   - Phase 1 (sélection héros) : signalée via CountDownLatch fourni par GameController.
 *   - Phase 2 (tour de jeu) : bloquée sur un SynchronousQueue ; ActionController dépose
 *     un signal quand l'action du joueur est validée.
 * 
 * @author Tom David et Florian Pépin.
 * @version 3.0
 */
public class PlayerGUI implements Player {
    
    private final GameController gameController;
    private final BattlefieldModel battlefield;
    private final String name;
    private final PlayerFrame frame;
    private boolean myTurn;

    /** Canal de synchronisation inter-threads pour la phase de tour (Phase 2). */
    private final SynchronousQueue<Boolean> turnSignal = new SynchronousQueue<>();

    /** Verrou décrémenté lors de la confirmation du héros (Phase 1). */
    private final CountDownLatch heroSelectionLatch;

    public PlayerGUI(GameController gameController, BattlefieldModel battlefield, String name, CountDownLatch heroSelectionLatch) {
        this.gameController = gameController;
        this.battlefield = battlefield;
        this.name = name;
        this.myTurn = false;
        this.heroSelectionLatch = heroSelectionLatch;
        this.frame = new PlayerFrame(gameController, battlefield, this, name);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public boolean isMyTurn() {
        return this.myTurn;
    }
    
    public void setMyTurn(boolean state) {
        this.myTurn = state;
        if (state) {
            this.frame.setBorderColorGUI(Color.GREEN);
        } else {
            this.frame.setBorderColorGUI(Color.BLACK);
        }
    }
    
    @Override
    public void showLeaderboard() {
        this.frame.showLeaderboard();
    }

    public void showGameplay() {
        this.frame.showGameplay();
    }

    public void showWaitingScreen() {
        this.frame.showWaitingScreen();
    }

    /**
     * Bloque le thread de jeu jusqu'à ce que le joueur GUI valide une action (Phase 2).
     * Débloqué par {@link #signalTurnDone()} appelé depuis le thread Swing.
     */
    @Override
    public void takeMyTurn() {
        this.setMyTurn(true);
        try {
            this.turnSignal.take(); // Attend que ActionController signale la fin du tour
        } catch (InterruptedException ex) {
            // Partie arrêtée : on sort proprement
            Thread.currentThread().interrupt();
        }
        this.setMyTurn(false);
    }

    /**
     * Appelé par ActionController (thread Swing) pour débloquer le thread de jeu
     * après qu'une action valide a été exécutée.
     */
    public void signalTurnDone() {
        try {
            this.turnSignal.put(true);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Appelé par HeroSelectionController après confirmation du héros (Phase 1).
     * Affiche l'écran d'attente et décrémente le CountDownLatch du GameController.
     */
    public void heroSelected() {
        this.showWaitingScreen();
        if (this.heroSelectionLatch != null) {
            this.heroSelectionLatch.countDown();
        }
    }

    @Override
    public boolean hasHero() {
        return this.battlefield.getCurrentHero() != null;
    }

    public void disposeFrame() {
        this.frame.disposeFrame();
    }
    
}