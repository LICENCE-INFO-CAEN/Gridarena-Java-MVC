package gridarena;

import gridarena.view.gui.AdminPanel;

import javax.swing.SwingUtilities;

/**
 * Classe principale d'entrée servant à lancer le panneau d'administration du jeu.
 *
 * @author Florian Pépin.
 * @version 2.0
 */
public class Main {
    
    /**
     * Point d'entrée de l'application.
     * Déploie l'interface d'administration Swing.
     * 
     * @param args arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminPanel();
        });
    }
    
}
