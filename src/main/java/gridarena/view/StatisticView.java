package gridarena.view;

import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldProxy;
import gridarena.utils.ModelListener;

import javax.swing.*;
import java.awt.*;

/**
 * Représente la vue des statistiques du joueur.
 * 
 * @author Tom David.
 * @version 1.0
 */
public class StatisticView extends JPanel implements ModelListener {
    
    private BattlefieldProxy battlefield;
    private JLabel stats;

    public StatisticView(BattlefieldProxy battlefield, String name) {
        super();
        this.battlefield = battlefield;
        this.battlefield.addModelListener(this);
        
        this.setPreferredSize(new Dimension(200, 100));
        this.setLayout(new GridLayout(1, 1));
        
        Hero hero = this.battlefield.getHero();
        JLabel l1 = new JLabel("Nom : " + name + " | Specialiste : " + hero.getSpecialization());
        JLabel l2 = new JLabel(new ImageIcon(getClass().getResource(hero.getImageUrl())));
        this.stats = new JLabel("Vie : " + hero.getHealthRemaining()+"/"+hero.getHealthMax()+" | Bouclier : " + (hero.isImmune() ? "actif" : "inactif"));
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        l2.setHorizontalAlignment(SwingConstants.CENTER);
        this.stats.setHorizontalAlignment(SwingConstants.LEFT);
        this.add(this.stats, SwingConstants.CENTER);
        this.add(l2, SwingConstants.CENTER); 
        this.add(l1, SwingConstants.CENTER);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
    }
    
    @Override
    public void updatedModel(Object source) {
        Hero hero = this.battlefield.getHero();
        this.stats.setText("Vie : " + hero.getHealthRemaining()+"/"+hero.getHealthMax()+" | Bouclier : " + (hero.isImmune() ? "actif" : "inactif"));
    }

}