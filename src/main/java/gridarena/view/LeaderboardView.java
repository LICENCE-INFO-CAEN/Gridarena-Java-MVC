package gridarena.view;

import gridarena.entity.hero.*;
import gridarena.model.*;

import javax.swing.*;
import java.awt.*;

/**
 * Représente la vue de la fin du jeu.
 * 
 * @author Tom David et Florian Pépin.
 * @version 1.0
 */
public class LeaderboardView extends JPanel {
    
    private BattlefieldProxy battlefield;

    public LeaderboardView(BattlefieldProxy battlefield) {
        super(new BorderLayout());
        this.battlefield = battlefield;
        
        Hero hero = this.battlefield.getHero();
        GroupHeroesArrayList group = new GroupHeroesArrayList();
        group.addHero(hero);
        
        JLabel l1 = new JLabel("VOS STATISTIQUES PERSONNELLES");
        JLabel l2 = new JLabel(hero.isAlive() ? "VOUS AVEZ GAGNÉ !" : "VOUS AVEZ PERDU !");
        l1.setHorizontalAlignment(SwingConstants.CENTER);
        l2.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(l1, BorderLayout.NORTH);
        this.add(new JScrollPane(new JTable(new GroupHeroesToTableModelAdapter(group))), BorderLayout.CENTER);
        this.add(l2, BorderLayout.SOUTH);
        this.setVisible(true);
    }
    
}
