package gridarena.view;

import gridarena.entity.Entity;
import gridarena.entity.consumable.*;
import gridarena.entity.environment.*;
import gridarena.entity.explosive.*;
import gridarena.entity.hero.*;
import gridarena.model.*;
import gridarena.utils.ModelListener;

import javax.swing.*;
import java.awt.*;

/**
 * Représente la vue de la grille de jeu.
 * 
 * @author Tom David et Florian Pépin.
 * @version 1.0
 */
public class BattlefieldView extends JPanel implements ModelListener {
    
    private BattlefieldModel battlefield;
    
    public BattlefieldView(BattlefieldModel battlefield) {
        super();
        this.battlefield = battlefield;
        this.battlefield.addModelListener(this);
        this.setPreferredSize(new Dimension(200, 200));
    }
    
    /**
     * Affichage de la grille de jeu.
     * 
     * @param g Graphics
     * @param map du jeu.
     * @param currentHero héro à montrer en surbrillance.
     */
    public void displayMap(Graphics g, Entity[][] map, Hero currentHero) {
        g.setColor(Color.BLACK);
        if (map != null) {
            int cellWidth = getWidth() / map[0].length;
            int cellHeight = getHeight() / map.length;
            
            Image background = new ImageIcon(getClass().getResource("/resources/images/ground.png")).getImage();
            
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    int x = j * cellWidth;
                    int y = i * cellHeight;
                    g.drawImage(background, x, y, cellWidth, cellHeight, null);
                    g.drawRect(x, y, cellWidth, cellHeight);
                    if (map[i][j] == null) {
                        g.drawString("", x + cellWidth / 2, y + cellHeight / 2);
                    } else {
                        String imageUrl = map[i][j].getImageUrl();
                        Image img = new ImageIcon(getClass().getResource(imageUrl)).getImage();
                        if (map[i][j] instanceof Barrel) {
                            g.drawImage(img, (x + cellWidth / 2)-20, y + cellHeight / 5 - 6, 22 * 2, 26 * 2, null);
                        } else if (map[i][j] instanceof Wall) {
                            g.drawImage(img, x+8, y, cellWidth-15, cellHeight, null);
                        } else if (map[i][j] instanceof Explosive) {
                            g.drawImage(img, x + cellWidth / 5, y + cellHeight / 5, cellWidth-20, cellHeight-20, null);
                        } else if (map[i][j] instanceof Consumable) {
                            g.drawImage(img, x + cellWidth / 5, y + cellHeight / 5, 50, 50, null);
                        } else if(map[i][j] instanceof Hero) {
                            Hero hero = (Hero) map[i][j];
                            g.drawImage(img, x+20, y+9, 13 * 2, 26 * 2, null);
                        }
                    }

                }
            }
            if (currentHero != null) {
                  g.setColor(Color.GREEN);
                  g.drawRect(currentHero.getY()*cellWidth, currentHero.getX()*cellHeight, cellWidth, cellHeight);
                  g.setColor(Color.BLACK);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        if (this.battlefield instanceof Battlefield) {
            this.displayMap(g, this.battlefield.getGrid(), this.battlefield.getCurrentHero());
        } else {
            this.displayMap(g, this.battlefield.getGrid(), ((BattlefieldProxy)this.battlefield).getHero());   
        }
    }
    
    @Override
    public void updatedModel(Object source) {
        this.repaint();
    }
    
}