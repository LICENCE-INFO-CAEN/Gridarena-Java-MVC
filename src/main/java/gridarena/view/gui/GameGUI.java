package gridarena.view.gui;

import gridarena.entity.hero.*;
import gridarena.model.*;
import gridarena.view.*;

import javax.swing.*;
import java.awt.*;

/**
 * Représente l'interface de jeu ayant une vue globale de toutes les entités.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class GameGUI extends JFrame {
    
    private Battlefield battlefield;
    
    public GameGUI(Battlefield battlefield) {
        super("GUI : Développeur");
        this.battlefield = battlefield;
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        
        JScrollPane scrollPane = new JScrollPane(new JTable(new GroupHeroesToTableModelAdapter(this.battlefield.getHeroes())));
        scrollPane.setPreferredSize(new Dimension(100, 100));
        contentPanel.add(scrollPane, BorderLayout.NORTH);
        contentPanel.add(new BattlefieldView(this.battlefield), BorderLayout.CENTER);
        this.add(contentPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * Crée une interface graphique general.
     *
     * @param battlefield le champ de bataille à afficher.
     * @return une instance de GameGUI.
     */
    public static GameGUI createGameGUI(Battlefield battlefield) {
        return new GameGUI(battlefield);
    }
    
}
