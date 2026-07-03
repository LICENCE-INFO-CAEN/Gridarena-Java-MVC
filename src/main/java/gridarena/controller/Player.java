package gridarena.controller;

/**
 * Représente le joueur qui joue au jeu.
 * 
 * @author Tom David, Florian Pépin.
 * @version 2.0
 */
public interface Player {

    /**
     * Obtenir le nom du joueur.
     * 
     * @return le nom du joueur.
     */
    String getName();
    
    /**
     * C'est le tour du joueur.
     */
    void takeMyTurn();

    /**
     * Afficher le classement de fin de partie.
     * Par défaut, ne fait rien (ex: pour les robots).
     */
    default void showLeaderboard() {}

    /**
     * Vérifier si le joueur a un héros.
     *
     * @return true si le héros est présent, false sinon.
     */
    boolean hasHero();

    /**
     * Libère les ressources du joueur (ex: fermeture des fenêtres).
     * Par défaut, ne fait rien.
     */
    default void cleanUp() {}

    /**
     * Affiche l'écran de gameplay.
     * Par défaut, ne fait rien.
     */
    default void showGameplay() {}
}
