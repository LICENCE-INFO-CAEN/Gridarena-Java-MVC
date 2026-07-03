package gridarena.view.gui;

import gridarena.controller.ActionController;
import gridarena.controller.GameController;
import gridarena.controller.HeroSelectionController;
import gridarena.model.BattlefieldModel;
import gridarena.view.BattlefieldView;
import gridarena.view.LeaderboardView;
import gridarena.view.StatisticView;
import gridarena.view.UITheme;

import gridarena.controller.gui.PlayerGUIController;

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
    private final PlayerGUIController playerGUI;
    private final String name;

    public PlayerFrame(GameController gameController, BattlefieldModel battlefield, PlayerGUIController playerGUI, String name) {
        super("Grid Arena : " + name);
        this.gameController = gameController;
        this.battlefield = battlefield;
        this.playerGUI = playerGUI;
        this.name = name;
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, UITheme.ACCENT));
        this.setSize(800, 600);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(UITheme.BG_PRIMARY);
        
        HeroSelectionController controllerHeroSelection = new HeroSelectionController(this.battlefield, this.gameController, this.playerGUI);
        this.add(controllerHeroSelection, BorderLayout.CENTER);

        this.setVisible(true);
    }
    
    public void updateTurnBorder(boolean isMyTurn) {
        Color borderColor = isMyTurn ? UITheme.SUCCESS : UITheme.ACCENT;
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, borderColor));
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
        waitPanel.setBackground(UITheme.BG_PRIMARY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 8, 0);
        JLabel waitLabel = new JLabel("EN ATTENTE", SwingConstants.CENTER);
        waitLabel.setFont(UITheme.FONT_TITLE);
        waitLabel.setForeground(UITheme.TEXT_PRIMARY);
        waitPanel.add(waitLabel, gbc);
        gbc.gridy = 1;
        JLabel subLabel = new JLabel("autres joueurs en cours de sélection...", SwingConstants.CENTER);
        subLabel.setFont(UITheme.FONT_BODY);
        subLabel.setForeground(UITheme.ACCENT);
        waitPanel.add(subLabel, gbc);
        this.add(waitPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    public void disposeFrame() {
        this.dispose();
    }
}
