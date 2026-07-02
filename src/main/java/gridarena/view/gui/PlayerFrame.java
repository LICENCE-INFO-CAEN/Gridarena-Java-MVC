package gridarena.view.gui;

import gridarena.controller.ActionController;
import gridarena.controller.GameController;
import gridarena.controller.HeroSelectionController;
import gridarena.model.BattlefieldModel;
import gridarena.view.BattlefieldView;
import gridarena.view.LeaderboardView;
import gridarena.view.StatisticView;

import javax.swing.*;
import java.awt.*;

/**
 * Composant de fenêtre JFrame gérant l'affichage graphique d'un joueur.
 *
 * @author Tom David, Florian Pépin.
 * @version 1.0
 */
public class PlayerFrame extends JFrame {
    
    private final BattlefieldModel battlefield;
    private final GameController gameController;
    private final PlayerGUI playerGUI;
    private final String name;

    public PlayerFrame(GameController gameController, BattlefieldModel battlefield, PlayerGUI playerGUI, String name) {
        super("Grid Arena : " + name);
        this.gameController = gameController;
        this.battlefield = battlefield;
        this.playerGUI = playerGUI;
        this.name = name;
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));
        this.setSize(800, 600);
        this.setLayout(new BorderLayout());
        
        HeroSelectionController controllerHeroSelection = new HeroSelectionController(this.battlefield, this.gameController, this.playerGUI);
        this.add(controllerHeroSelection, BorderLayout.CENTER);

        this.setVisible(true);
    }
    
    public void setBorderColorGUI(Color color) {
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, color));
    }
    
    public void showLeaderboard() {
        this.getContentPane().removeAll();
        this.add(new LeaderboardView(this.battlefield));
        this.revalidate();
        this.repaint();
    }

    public void showGameplay() {
        this.getContentPane().removeAll();
        BattlefieldView viewBattlefield = new BattlefieldView(this.battlefield);
        this.add(viewBattlefield, BorderLayout.CENTER);
        StatisticView viewStatistic = new StatisticView(this.battlefield, this.name);
        this.add(viewStatistic, BorderLayout.NORTH);
        ActionController controllerAction = new ActionController(this.battlefield, this.gameController, this.playerGUI);
        this.add(controllerAction, BorderLayout.WEST);
        this.revalidate();
        this.repaint();
    }

    public void showWaitingScreen() {
        this.getContentPane().removeAll();
        JPanel waitPanel = new JPanel(new GridBagLayout());
        waitPanel.setBackground(new Color(15, 23, 42)); // Slate 900
        JLabel waitLabel = new JLabel("En attente du choix des autres joueurs...", SwingConstants.CENTER);
        waitLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        waitLabel.setForeground(new Color(245, 158, 11)); // Amber 500
        waitPanel.add(waitLabel);
        this.add(waitPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    public void disposeFrame() {
        this.dispose();
    }
}
