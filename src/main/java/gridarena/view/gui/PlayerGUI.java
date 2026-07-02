package gridarena.view.gui;

import gridarena.controller.ActionController;
import gridarena.controller.GameController;
import gridarena.controller.HeroSelectionController;
import gridarena.model.BattlefieldProxy;
import gridarena.view.*;

import javax.swing.*;
import java.awt.*;

/**
 * Représente l'interface utilisateur d'un joueur graphique.
 * 
 * @author Tom David et Florian Pépin.
 * @version 1.0
 */
public class PlayerGUI extends JFrame implements PlayerWithLeaderboard {
    
    private GameController gameController;
    private BattlefieldProxy battlefield;
    private String name;
    private boolean myTurn;

    public PlayerGUI(GameController gameController, BattlefieldProxy battlefield, String name) {
        super("Grid Arena : "+name);
        this.gameController = gameController;
        this.battlefield = battlefield;
        this.name = name;
        this.myTurn = false;
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));
        this.setSize(800, 600);
        this.setLayout(new BorderLayout());
        
        HeroSelectionController controllerHeroSelection = new HeroSelectionController(this.battlefield, this.gameController, this);
        this.add(controllerHeroSelection, BorderLayout.CENTER);

        this.setVisible(true);
    }
    
    public void setBorderColorGUI(Color color) {
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, color));
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public boolean isMyTurn() {
        return myTurn;
    }
    
    public void setMyTurn(boolean state) {
        if(state) {
            this.setBorderColorGUI(Color.GREEN);
            this.myTurn = true;
        } else {
            this.setBorderColorGUI(Color.BLACK);
            this.myTurn = false;
        }
    }
    
    @Override
    public void showLeaderboard() {
        this.getContentPane().removeAll();
        this.add(new LeaderboardView(this.battlefield));
        this.revalidate();
        this.repaint();
    }

    /**
     * Affiche la partie principale du jeu.
     */
    public void showGameplay() {
        this.getContentPane().removeAll();
        BattlefieldView viewBattlefield = new BattlefieldView(this.battlefield);
        this.add(viewBattlefield, BorderLayout.CENTER);
        StatisticView viewStatistic = new StatisticView(this.battlefield, this.name);
        this.add(viewStatistic, BorderLayout.NORTH);
        ActionController controllerAction = new ActionController(this.battlefield, this.gameController, this);
        this.add(controllerAction, BorderLayout.WEST);
        this.revalidate();
        this.repaint();
    }
    
    @Override
    public void takeMyTurn() {
        this.setMyTurn(true);
        synchronized (this.gameController) {
            try {
                this.gameController.wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}