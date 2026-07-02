package gridarena.controller.bot;

import gridarena.model.BattlefieldModel;

/**
 * Représente la stratégie d'un joueur robot.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public interface BotStrategy {
    
    /**
     * Définit la stratégie d'action du bot.
     * 
     * @param battlefieldModel Le modèle du champ de bataille utilisé pour interagir.
     */
    void actionStrategy(BattlefieldModel battlefieldModel);
    
}