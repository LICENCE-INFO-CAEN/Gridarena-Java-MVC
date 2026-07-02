package gridarena.model;

import gridarena.entity.Entity;
import gridarena.entity.hero.*;
import gridarena.utils.ListenableModel;

/**
 * Représente le contrat du champ de bataille (modèle).
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public interface BattlefieldModel extends ListenableModel {
    
    /**
     * Obtenir le héro qui est en train d'être joué.
     * 
     * @return le hero en question.
     */
    Hero getCurrentHero();
    
    /**
     * Changer le hero qui est en train d'être joué par un autre.
     * 
     * @param i indice du nouveau héro qui est en train d'être joué.
     */
    void setCurrentHero(int i);
    
    /**
     * Obtenir la liste des héros.
     * 
     * @return la liste des heros.
     */
    GroupHeroesArrayList getHeroes();
    
    /**
     * Ajouter un héro dans la grille.
     * 
     * @param specialization du hero à ajouter.
     * @return le hero ajouté.
     */
    Hero addHero(String specialization);
    
    /**
     * Obtenir la grille de jeu.
     * 
     * @return la grille de jeu.
     */
    Entity[][] getGrid();
    
    /**
     * Obtenir la taille de la grille.
     * 
     * @return la taille de la grille.
     */
    int getSize(); 
    
    /**
     * Ajoute un explosif dans la grille.
     *
     * @param h le héro qui dépose la bombe.
     * @param direction dans laquelle le joueur veut déployer une bombe.
     * @param explosive le type d'explosif à ajouter dans la grille.
     * @return true si l'explosif est posé sinon false.
     */
    boolean addExplosive(Hero h, String direction, String explosive);

    /**
     * Déplace un joueur à une nouvelle position sur la grille.
     *
     * @param h Le héro à déplacer.
     * @param direction dans laquelle le joueur souhaite se diriger.
     * @return true si le joueur a été déplacé sinon false.
     */
    boolean moveHero(Hero h, String direction);

    /**
     * Active le bouclier du hero.
     * 
     * @param h hero qui active son bouclier.
     * @return true si le hero a pu activer son bouclier sinon false.
     */
    boolean activateShield(Hero h);
    
    /**
     * Tirer sur un joueur ou sur un barril.
     * Règle du jeu : tir horizontal ou vertical.
     * 
     * @param h le hero qui tire.
     * @param d direction du tir.
     * @return true si le joueur à touché un joueur ou un barril sinon false.
     */
    boolean shootHero(Hero h, String d);
    
    /**
     * Frapper un joueur avec une hache.
     * Règle du jeu : frapper horizontalement ou verticalement.
     * 
     * @param h le hero qui frappe.
     * @param d direction du coup.
     * @return true si le joueur à touché un joueur sinon false.
     */
    boolean axAttack(Hero h, String d);
}
