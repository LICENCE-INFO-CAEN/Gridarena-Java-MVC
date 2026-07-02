package gridarena.controller;

import gridarena.controller.bot.*;
import gridarena.entity.hero.*;
import gridarena.model.*;
import gridarena.model.fillgrid.*;
import gridarena.view.*;
import gridarena.view.gui.*;
import gridarena.view.cli.*;

import java.util.*;

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
    
    public GameController(boolean showOverview, int guiPlayers, int cliPlayers, int botPlayers, int sizeGrid, int walls, int medicalKits, int ammoKits, int barrels, FillStrategy fillStrategy) {
        this.battlefield = new Battlefield(sizeGrid, walls, medicalKits, ammoKits, barrels, fillStrategy);
        this.players = new ArrayList<>();
        this.playersHistory = new ArrayList<>();
        this.guiPlayers = guiPlayers;
        this.cliPlayers = cliPlayers;
        this.botPlayers = botPlayers;
        this.playersCounter = 0;
        this.currentPlayer = 0;
        if (showOverview) {
            GameGUI.createGameGUI(this.battlefield);
        }
    }
    
    /**
     * Démarrer une partie de jeu.
     * 
     * @param botStrategy strategie du bot.
     */
    public void demarrer(BotStrategy botStrategy) {
        for (int i = 0; i < this.guiPlayers+this.cliPlayers+this.botPlayers; i++) {
            if (i < this.guiPlayers) {
                this.addPlayer(new PlayerGUI(this, new BattlefieldProxy(this.battlefield), "J"+(i+1)));   
            } else if (i < this.guiPlayers+this.cliPlayers) {
                this.addPlayer(new PlayerCLI(this, new BattlefieldProxy(this.battlefield), "J"+(i+1)));
            } else {
                this.addPlayer(new PlayerBot(this, new BattlefieldProxy(this.battlefield), "Bot"+(i+1), botStrategy));
            }
        }
        new Thread(this).start();
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
        GroupHeroesArrayList heroes = this.battlefield.getHeroes();
        int previousPlayer = currentPlayer == 0 ? heroes.getSize()-1 : currentPlayer-1;
        if (heroes.getHero(previousPlayer).isImmune()) heroes.getHero(previousPlayer).setImmune(false);
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
    
    /**
     * Lancer le tour par tour.
     */
    public synchronized void run() {
        while (this.playersCounter > 1) {
            this.battlefield.setCurrentHero(this.findPlayer(this.players.get(this.currentPlayer)));
            this.players.get(this.currentPlayer).takeMyTurn();
            this.nextTurn(this.currentPlayer);
            this.currentPlayer++;
            if (this.currentPlayer >= this.players.size()) {
                this.currentPlayer = 0;
            }
        }
        for (Player player : this.playersHistory) {
            if (player instanceof PlayerWithLeaderboard) {
                ((PlayerWithLeaderboard) player).showLeaderboard();   
            }
        }
    }
    
}
