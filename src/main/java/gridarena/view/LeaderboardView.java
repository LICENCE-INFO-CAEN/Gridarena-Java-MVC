package gridarena.view;

import gridarena.entity.hero.*;
import gridarena.model.*;

import javax.swing.*;
import java.awt.*;

/**
 * Représente la vue de la fin du jeu.
 * 
 * @author Tom David, Florian Pépin.
 * @version 2.0
 */
public class LeaderboardView extends JPanel {
    
    private BattlefieldModel battlefield;

    public LeaderboardView(BattlefieldModel battlefield) {
        super(new BorderLayout(15, 15));
        this.battlefield = battlefield;
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setBackground(new Color(15, 23, 42)); // Slate 900
        
        Hero hero = this.battlefield.getCurrentHero();
        GroupHeroesArrayList group = new GroupHeroesArrayList();
        group.addHero(hero);
        
        JLabel l1 = new JLabel("VOS STATISTIQUES PERSONNELLES");
        l1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        l1.setForeground(new Color(245, 158, 11)); // Amber 500
        l1.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel l2 = new JLabel(hero.isAlive() ? "VOUS AVEZ GAGNÉ !" : "VOUS AVEZ PERDU !");
        l2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        if (hero.isAlive()) {
            l2.setForeground(new Color(16, 185, 129)); // Emerald Green
        } else {
            l2.setForeground(new Color(244, 63, 94)); // Rose Red
        }
        l2.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Configuration de la JTable
        JTable table = new JTable(new GroupHeroesToTableModelAdapter(group));
        table.setBackground(new Color(30, 41, 59)); // Slate 800
        table.setForeground(new Color(241, 245, 249)); // Slate 100
        table.setGridColor(new Color(51, 65, 85)); // Slate 700
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        
        table.getTableHeader().setBackground(new Color(15, 23, 42)); // Slate 900
        table.getTableHeader().setForeground(new Color(245, 158, 11)); // Amber 500
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(30, 41, 59)); // Slate 800
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));
        
        this.add(l1, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(l2, BorderLayout.SOUTH);
        this.setVisible(true);
    }
    
}
