package gridarena.controller;

import gridarena.model.BattlefieldModel;
import gridarena.view.gui.PlayerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Représente le contrôleur permettant de sélectionner son Hero au début de la partie.
 * 
 * @author Emilien Huron.
 * @version 1.0
 */
public class HeroSelectionController extends JPanel implements ActionListener {
    
    private BattlefieldModel battlefield;
    private GameController game;
    private PlayerGUI playerGUI;
    
    public HeroSelectionController(BattlefieldModel battlefield, GameController game, PlayerGUI playerGUI) {
        super(new GridLayout(2, 0));
        this.battlefield = battlefield;
        this.game = game;
        this.playerGUI = playerGUI;
        
        JLabel label = new JLabel("Veuillez choisir la spécialité de votre héro :");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton buttonMastodonte = new JButton("Mastodonte");
        buttonMastodonte.setPreferredSize(new Dimension(125, 45));
        JButton buttonWarrior = new JButton("Warrior");
        buttonWarrior.setPreferredSize(new Dimension(125, 45));
        JButton buttonAssassin = new JButton("Assassin");
        buttonAssassin.setPreferredSize(new Dimension(125, 45));
        buttonMastodonte.addActionListener(this);
        buttonWarrior.addActionListener(this);
        buttonAssassin.addActionListener(this);
        panel.add(buttonMastodonte, BorderLayout.CENTER);
        panel.add(buttonWarrior, BorderLayout.CENTER);
        panel.add(buttonAssassin, BorderLayout.CENTER);
        this.add(label, BorderLayout.CENTER);
        this.add(panel, BorderLayout.CENTER);
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!playerGUI.isMyTurn()) {
            return;
        }
        JButton sourceButton = (JButton) e.getSource();
        String buttonText = sourceButton.getText();
        this.battlefield.addHero(buttonText.toLowerCase());
        this.playerGUI.showGameplay();
        playerGUI.setMyTurn(false);
        synchronized (this.game) {
            this.game.notifyAll();
        }
    }
    
}
