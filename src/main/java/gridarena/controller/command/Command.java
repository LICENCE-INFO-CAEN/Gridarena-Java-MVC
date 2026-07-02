package gridarena.controller.command;

/**
 * Interface représentant une commande d'action du jeu.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public interface Command {
    
    /**
     * Exécute l'action encapsulée par la commande.
     *
     * @return true si l'action a été exécutée avec succès, false sinon.
     */
    boolean execute();
    
}
