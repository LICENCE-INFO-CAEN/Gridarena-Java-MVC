package gridarena.model;

/**
 * Validateur métier pour la configuration d'une partie de jeu.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class GameConfigValidator {

    /**
     * Valide un objet GameConfig.
     * Le non-respect des contraintes lève une exception descriptive.
     *
     * @param config les paramètres à valider
     * @throws IllegalArgumentException si les proportions ou les règles ne sont pas respectées
     */
    public static void validate(GameConfig config) throws IllegalArgumentException {
        int totalPlayers = config.getGuiPlayers() + config.getCliPlayers() + config.getBotPlayers();
        if (totalPlayers == 0) {
            throw new IllegalArgumentException("Il faut au moins 1 joueur pour lancer une partie !");
        }
        if (totalPlayers * 2 > config.getGridSize()) {
            throw new IllegalArgumentException("Trop de joueurs pour la taille de grille choisie (règle : joueurs * 2 <= taille grille).");
        }
        if (config.getWalls() > config.getGridSize()) {
            throw new IllegalArgumentException("Le nombre de murs dépasse la taille de la grille.");
        }
        if (config.getMedKits() > config.getGridSize()) {
            throw new IllegalArgumentException("Le nombre de kits de soin dépasse la taille de la grille.");
        }
        if (config.getAmmoKits() > config.getGridSize()) {
            throw new IllegalArgumentException("Le nombre de kits de munitions dépasse la taille de la grille.");
        }
        if (config.getBarrels() > config.getGridSize()) {
            throw new IllegalArgumentException("Le nombre de barils dépasse la taille de la grille.");
        }
    }
}
