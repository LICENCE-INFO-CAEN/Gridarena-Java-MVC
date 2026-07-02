package gridarena.controller.state;

import gridarena.controller.command.Command;
import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;

/**
 * Interface représentant l'état du contrôleur d'action de l'interface graphique.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public interface ControllerState {
    
    /**
     * Génère la commande associée à cet état pour la direction donnée.
     *
     * @param battlefield le modèle du champ de bataille
     * @param hero le héros exécutant l'action
     * @param direction la direction choisie
     * @return la commande d'action correspondante
     */
    Command getCommandForDirection(BattlefieldModel battlefield, Hero hero, String direction);
    
    /**
     * Retourne le nom de l'état.
     *
     * @return le nom affiché de l'état
     */
    String getName();
    
}
