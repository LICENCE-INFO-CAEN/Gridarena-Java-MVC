package gridarena.view;

import gridarena.entity.hero.*;
import gridarena.model.*;
import gridarena.view.UITheme;

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
        this.setBackground(UITheme.BG_PRIMARY);
        
        Hero hero = this.battlefield.getCurrentHero();
        GroupHeroesArrayList group = new GroupHeroesArrayList();
        group.addHero(hero);
        
        JLabel l1 = new JLabel("STATISTIQUES DE PARTIE", SwingConstants.CENTER);
        l1.setFont(UITheme.FONT_LABEL);
        l1.setForeground(UITheme.TEXT_SECONDARY);
        l1.setHorizontalAlignment(SwingConstants.CENTER);
        
        String resultText = hero.isAlive() ? "VICTOIRE" : "DÉFAITE";
        JLabel l2 = new JLabel(resultText, SwingConstants.CENTER);
        l2.setFont(UITheme.FONT_TITLE);
        l2.setForeground(hero.isAlive() ? UITheme.SUCCESS : UITheme.ACCENT);
        l2.setHorizontalAlignment(SwingConstants.CENTER);
        
        JTable table = new JTable(new GroupHeroesToTableModelAdapter(group));
        table.setBackground(UITheme.BG_SECONDARY);
        table.setForeground(UITheme.TEXT_PRIMARY);
        table.setGridColor(UITheme.BORDER);
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(28);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(UITheme.ACCENT);
        table.getTableHeader().setForeground(UITheme.TEXT_PRIMARY);
        table.getTableHeader().setFont(UITheme.FONT_LABEL);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(UITheme.BG_SECONDARY);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        
        // Panneau header avec badge
        JPanel headerPanel = new JPanel(new BorderLayout(0, 6));
        headerPanel.setOpaque(false);
        headerPanel.add(l2, BorderLayout.CENTER);
        headerPanel.add(l1, BorderLayout.SOUTH);
        
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }
    
}
