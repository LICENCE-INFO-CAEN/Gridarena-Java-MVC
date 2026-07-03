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
            int boardSize = Math.min(getWidth(), getHeight());
            int cellWidth = boardSize / map[0].length;
            int cellHeight = boardSize / map.length;
            
            // Taille réelle calculée pour le plateau
            int actualBoardWidth = cellWidth * map[0].length;
            int actualBoardHeight = cellHeight * map.length;
            
            // Centrage de la grille
            int startX = (getWidth() - actualBoardWidth) / 2;
            int startY = (getHeight() - actualBoardHeight) / 2;
            
            Image background = new ImageIcon(getClass().getResource("/resources/images/ground.png")).getImage();
            
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    int x = startX + j * cellWidth;
                    int y = startY + i * cellHeight;
                    g.drawImage(background, x, y, cellWidth, cellHeight, null);
                    
                    g.setColor(new Color(0x3A, 0x3A, 0x3C, 80)); // BORDER semi-transparent
                    g.drawRect(x, y, cellWidth, cellHeight);
                    
                    if (map[i][j] != null) {
                        String imageUrl = map[i][j].getImageUrl();
                        Image img = new ImageIcon(getClass().getResource(imageUrl)).getImage();
                        if (map[i][j] instanceof Wall) {
                            // Les murs remplissent entièrement la case pour se toucher correctement
                            g.drawImage(img, x, y, cellWidth, cellHeight, null);
                        } else {
                            // Détermination du facteur d'échelle selon le type d'entité
                            double scaleFactor = 0.75;
                            if (map[i][j] instanceof Hero) {
                                scaleFactor = 0.85;
                            } else if (map[i][j] instanceof Explosive) {
                                scaleFactor = 0.6;
                            } else if (map[i][j] instanceof Consumable) {
                                scaleFactor = 0.65;
                            }

                            int maxW = (int) (cellWidth * scaleFactor);
                            int maxH = (int) (cellHeight * scaleFactor);

                            int imgWidth = img.getWidth(null);
                            int imgHeight = img.getHeight(null);
                            if (imgWidth <= 0 || imgHeight <= 0) {
                                imgWidth = 50;
                                imgHeight = 50;
                            }

                            double imgRatio = (double) imgWidth / imgHeight;
                            double cellRatio = (double) maxW / maxH;

                            int drawW, drawH;
                            if (imgRatio > cellRatio) {
                                drawW = maxW;
                                drawH = (int) (maxW / imgRatio);
                            } else {
                                drawH = maxH;
                                drawW = (int) (maxH * imgRatio);
                            }

                            int drawX = x + (cellWidth - drawW) / 2;
                            int drawY = y + (cellHeight - drawH) / 2;

                            g.drawImage(img, drawX, drawY, drawW, drawH, null);
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
                    startX + currentHero.getY() * cellWidth + 2,
                    startY + currentHero.getX() * cellHeight + 2,
                    cellWidth - 4, cellHeight - 4
                );
                // Coin haut-gauche accent
                g2.setColor(UITheme.ACCENT_HOVER);
                g2.setStroke(new BasicStroke(4f));
                g2.drawLine(
                    startX + currentHero.getY() * cellWidth + 2,
                    startY + currentHero.getX() * cellHeight + 2,
                    startX + currentHero.getY() * cellWidth + 10,
                    startY + currentHero.getX() * cellHeight + 2
                );
                g2.drawLine(
                    startX + currentHero.getY() * cellWidth + 2,
                    startY + currentHero.getX() * cellHeight + 2,
                    startX + currentHero.getY() * cellWidth + 2,
                    startY + currentHero.getX() * cellHeight + 10
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