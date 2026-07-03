package gridarena.controller;

import gridarena.controller.bot.*;
import gridarena.entity.hero.*;
import gridarena.model.*;
import gridarena.model.fillgrid.*;
import gridarena.view.*;
import gridarena.view.gui.*;
import gridarena.view.cli.*;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;

/**
 * Représente le contrôleur mère du jeu.
 * 
 * @author Emilien Huron.
 * @version 1.0
 */
public class GameController implements Runnable {

    private Battlefield battlefield;
    private List<Player> players;
    private List<Player> playersHistory;
    private int guiPlayers, cliPlayers, botPlayers;
    private int playersCounter;
    public int currentPlayer;
    private boolean gameStarted = false;
    private volatile boolean running = true;
    private Thread gameThread;
    private CountDownLatch heroSelectionLatch;
    private Consumer<String> onGameFinishedCallback;

    public void setOnGameFinished(Consumer<String> callback) {
        this.onGameFinishedCallback = callback;
    }
    
    public GameController(boolean showOverview, int guiPlayers, int cliPlayers, int botPlayers, int sizeGrid, int walls, int medicalKits, int ammoKits, int barrels, FillStrategy fillStrategy) {
        this.battlefield = new Battlefield(sizeGrid, walls, medicalKits, ammoKits, barrels, fillStrategy);
        this.players = new ArrayList<>();
        this.playersHistory = new ArrayList<>();
        this.guiPlayers = guiPlayers;
        this.cliPlayers = cliPlayers;
        this.botPlayers = botPlayers;
        this.playersCounter = 0;
        this.currentPlayer = 0;
    }
    
    public boolean isGameStarted() {
        return this.gameStarted;
    }

    public boolean allPlayersHaveSelectedHero() {
        for (Player p : this.players) {
            if (!p.hasHero()) {
                return false;
            }
        }
        return true;
    }

    public void stop() {
        this.running = false;
        this.gameStarted = false;
        for (Player p : this.playersHistory) {
            if (p instanceof PlayerGUI) {
                ((PlayerGUI) p).disposeFrame();
            }
        }
        // Interrompt le thread de jeu :
        //  - débloque CountDownLatch.await() (Phase 1)
        //  - débloque SynchronousQueue.take()  (Phase 2 via PlayerGUI.takeMyTurn)
        //  - débloque Thread.sleep()           (PlayerBot)
        if (this.gameThread != null) {
            this.gameThread.interrupt();
        }
    }

    public boolean isRunning() {
        return this.running;
    }

    public Battlefield getBattlefield() {
        return this.battlefield;
    }
    
    /**
     * Démarrer une partie de jeu.
     * 
     * @param botStrategy strategie du bot.
     */
    public void demarrer(BotStrategy botStrategy) {
        this.running = true;
        // Un CountDownLatch par GUI player : atteint 0 quand tous ont choisi leur héros
        this.heroSelectionLatch = new CountDownLatch(this.guiPlayers);
        for (int i = 0; i < this.guiPlayers+this.cliPlayers+this.botPlayers; i++) {
            if (i < this.guiPlayers) {
                this.addPlayer(new PlayerGUI(this, new BattlefieldProxy(this.battlefield), "J"+(i+1), this.heroSelectionLatch));
            } else if (i < this.guiPlayers+this.cliPlayers) {
                this.addPlayer(new PlayerCLI(this, new BattlefieldProxy(this.battlefield), "J"+(i+1)));
            } else {
                this.addPlayer(new PlayerBot(this, new BattlefieldProxy(this.battlefield), "Bot"+(i+1), botStrategy));
            }
        }
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }
    
    /**
     * Ajouter un joueur dans la liste du tour par tour.
     * 
     * @param p le joueur a ajouter.
     */
    public void addPlayer (Player p) {
        this.players.add(p);
        this.playersHistory.add(p);
        this.playersCounter ++;
    }
    
    /**
     * Supprimer un joueur de la liste du tour par tour.
     * Supprime le joueur si celui-ci est mort.
     */
    private void removePlayer() {
        GroupHeroesArrayList heroes = this.battlefield.getHeroes();
        if (heroes.getSize() == 0) {
            return;
        }
        for (int i = this.players.size()-1; i >=0; i--) {
            int j = this.findPlayer(this.players.get(i));
            if (j < heroes.getSize()) {
                if (!heroes.getHero(j).isAlive()) {
                    this.players.remove(i);
                    this.playersCounter --; 
                }
            }
        }
    }
    
    /**
     * Actions à faire lorsqu'un tour est terminé.
     * 
     * @param currentPlayer indice du joueur qui vient de jouer. 
     */
    private void nextTurn(int currentPlayer) {
        this.battlefield.decrementBombs();
        this.removePlayer();
    }
    
    /**
     * Chercher un joueur vivant dans l'historique des joueurs.
     * 
     * @param player a chercher.
     * @return l'indice du joueur dans l'historique.
     */
    private int findPlayer(Player player) {
        for (int i = 0; i < this.playersHistory.size(); i++) {
            if (player.getName().equals(this.playersHistory.get(i).getName())) {
                return i;
            }
        }
        throw new RuntimeException("Le joueur "+player.getName()+" n'a pas été trouvé.");
    }
    
    public void run() {
        // Phase 1 : Sélection des héros
        
        // Auto-sélection pour les robots
        for (Player p : this.players) {
            if (!this.running) return;
            if (p instanceof PlayerBot) {
                p.takeMyTurn();
            }
        }
        
        // Sélection séquentielle pour les joueurs console (partage de System.in)
        for (Player p : this.players) {
            if (!this.running) return;
            if (p instanceof PlayerCLI) {
                p.takeMyTurn();
            }
        }
        
        // Attente des joueurs graphiques qui choisissent en parallèle (CountDownLatch)
        try {
            this.heroSelectionLatch.await();
        } catch (InterruptedException e) {
            return; // Partie arrêtée pendant la sélection
        }

        if (!this.running) return;
        
        // Tous les héros sont sélectionnés, on démarre la partie !
        this.gameStarted = true;
        
        // Affichage du plateau de jeu principal pour tous les joueurs graphiques
        for (Player p : this.players) {
            if (p instanceof PlayerGUI) {
                ((PlayerGUI) p).showGameplay();
            }
        }
        
        // Phase 2 : Déroulement du jeu au tour par tour
        while (this.playersCounter > 1 && this.running) {
            int heroIndex = this.findPlayer(this.players.get(this.currentPlayer));
            Hero currentHero = this.battlefield.getHeroes().getHero(heroIndex);
            if (currentHero.isImmune()) {
                currentHero.setImmune(false);
            }
            this.battlefield.setCurrentHero(heroIndex);
            this.players.get(this.currentPlayer).takeMyTurn();
            if (!this.running) break;
            this.nextTurn(this.currentPlayer);
            this.currentPlayer++;
            if (this.currentPlayer >= this.players.size()) {
                this.currentPlayer = 0;
            }
        }
        if (this.running) {
            for (Player player : this.playersHistory) {
                player.showLeaderboard();
                if (player instanceof PlayerGUI) {
                    ((PlayerGUI) player).disposeFrame();
                }
            }
            if (this.onGameFinishedCallback != null) {
                String winnerName = null;
                if (this.players.size() == 1) {
                    winnerName = this.players.get(0).getName();
                }
                final String finalWinner = winnerName;
                SwingUtilities.invokeLater(() -> this.onGameFinishedCallback.accept(finalWinner));
            }
        }
    }
    
}
