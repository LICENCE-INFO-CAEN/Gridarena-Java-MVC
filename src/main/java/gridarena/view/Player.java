package gridarena.view;

/**
 * Représente le joueur qui joue au jeu.
 * 
 * @author Tom David.
 * @version 1.0
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
     */
    void showLeaderboard();

    /**
     * Vérifier si le joueur a un héros.
     *
     * @return true si le héros est présent, false sinon.
     */
    boolean hasHero();
}
