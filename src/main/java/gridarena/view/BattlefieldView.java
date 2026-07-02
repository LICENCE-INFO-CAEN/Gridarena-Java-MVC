package gridarena.view;

import gridarena.entity.Entity;
import gridarena.entity.consumable.*;
import gridarena.entity.environment.*;
import gridarena.entity.explosive.*;
import gridarena.entity.hero.*;
import gridarena.model.*;
import gridarena.utils.ModelListener;
import gridarena.view.UITheme;

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
        if (map != null) {
            int cellWidth = getWidth() / map[0].length;
            int cellHeight = getHeight() / map.length;
            
            Image background = new ImageIcon(getClass().getResource("/resources/images/ground.png")).getImage();
            
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    int x = j * cellWidth;
                    int y = i * cellHeight;
                    g.drawImage(background, x, y, cellWidth, cellHeight, null);
                    
                    g.setColor(new Color(0x3A, 0x3A, 0x3C, 80)); // BORDER semi-transparent
                    g.drawRect(x, y, cellWidth, cellHeight);
                    
                    if (map[i][j] != null) {
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
                            g.drawImage(img, x+20, y+9, 13 * 2, 26 * 2, null);
                        }
                    }
                }
            }
            if (currentHero != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Bordure crimson nette (pas de coin arrondi)
                g2.setColor(UITheme.ACCENT);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRect(
                    currentHero.getY() * cellWidth + 2,
                    currentHero.getX() * cellHeight + 2,
                    cellWidth - 4, cellHeight - 4
                );
                // Coin haut-gauche accent
                g2.setColor(UITheme.ACCENT_HOVER);
                g2.setStroke(new BasicStroke(4f));
                g2.drawLine(
                    currentHero.getY() * cellWidth + 2,
                    currentHero.getX() * cellHeight + 2,
                    currentHero.getY() * cellWidth + 10,
                    currentHero.getX() * cellHeight + 2
                );
                g2.drawLine(
                    currentHero.getY() * cellWidth + 2,
                    currentHero.getX() * cellHeight + 2,
                    currentHero.getY() * cellWidth + 2,
                    currentHero.getX() * cellHeight + 10
                );
                g2.dispose();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(UITheme.BG_PRIMARY);
        this.displayMap(g, this.battlefield.getGrid(), this.battlefield.getCurrentHero());
    }
    
    @Override
    public void updatedModel(Object source) {
        this.repaint();
    }
    
}