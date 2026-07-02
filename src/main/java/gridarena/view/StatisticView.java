package gridarena.view;

import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;
import gridarena.utils.ModelListener;

import javax.swing.*;
import java.awt.*;

/**
 * Représente la vue des statistiques du joueur.
 * 
 * @author Tom David, Florian Pépin.
 * @version 2.0
 */
public class StatisticView extends JPanel implements ModelListener {
    
    private BattlefieldModel battlefield;
    private JLabel stats;
    private JLabel l1;
    private JLabel l2;

    public StatisticView(BattlefieldModel battlefield, String name) {
        super();
        this.battlefield = battlefield;
        this.battlefield.addModelListener(this);
        
        this.setPreferredSize(new Dimension(800, 80));
        this.setLayout(new GridLayout(1, 3, 10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        Hero hero = this.battlefield.getCurrentHero();
        
        l1 = new JLabel("Joueur : " + name + " (" + hero.getSpecialization() + ")");
        l1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l1.setForeground(new Color(241, 245, 249)); // Slate 100
        l1.setHorizontalAlignment(SwingConstants.LEFT);
        
        l2 = new JLabel(new ImageIcon(getClass().getResource(hero.getImageUrl())));
        l2.setHorizontalAlignment(SwingConstants.CENTER);
        
        this.stats = new JLabel();
        this.stats.setFont(new Font("Segoe UI", Font.BOLD, 14));
        this.stats.setForeground(new Color(245, 158, 11)); // Amber 500
        this.stats.setHorizontalAlignment(SwingConstants.RIGHT);
        
        updateStatsText(hero);

        this.add(l1);
        this.add(l2);
        this.add(this.stats);
    }
    
    private void updateStatsText(Hero hero) {
        this.stats.setText("Vie : " + hero.getHealthRemaining() + "/" + hero.getHealthMax() + " | Bouclier : " + (hero.isImmune() ? "Actif" : "Inactif"));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(30, 41, 59)); // Slate 800
    }
    
    @Override
    public void updatedModel(Object source) {
        Hero hero = this.battlefield.getCurrentHero();
        if (hero != null) {
            updateStatsText(hero);
            l2.setIcon(new ImageIcon(getClass().getResource(hero.getImageUrl())));
        }
    }

}