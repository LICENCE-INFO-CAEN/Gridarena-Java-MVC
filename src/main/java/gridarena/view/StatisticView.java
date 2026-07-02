package gridarena.view;

import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;
import gridarena.utils.ModelListener;
import gridarena.view.UITheme;

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
        
        l1 = new JLabel("  " + name.toUpperCase() + "  —  " + hero.getSpecialization().toUpperCase());
        l1.setFont(UITheme.FONT_SUBTITLE);
        l1.setForeground(UITheme.TEXT_PRIMARY);
        l1.setHorizontalAlignment(SwingConstants.LEFT);
        
        l2 = new JLabel(new ImageIcon(getClass().getResource(hero.getImageUrl())));
        l2.setHorizontalAlignment(SwingConstants.CENTER);
        
        this.stats = new JLabel();
        this.stats.setFont(UITheme.FONT_SUBTITLE);
        this.stats.setForeground(UITheme.ACCENT);
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
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(UITheme.BG_SECONDARY);
        g2.fillRect(0, 0, getWidth(), getHeight());
        // Bordure basse crimson
        g2.setColor(UITheme.ACCENT);
        g2.fillRect(0, getHeight() - 2, getWidth(), 2);
        g2.dispose();
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