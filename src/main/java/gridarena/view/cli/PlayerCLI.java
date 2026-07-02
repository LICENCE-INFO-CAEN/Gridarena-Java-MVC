package gridarena.view.cli;

import gridarena.controller.*;
import gridarena.entity.hero.*;
import gridarena.model.*;
import gridarena.view.*;

import java.util.*;

/**
 * Représente l'interface utilisateur d'un joueur en ligne de commandes.
 *
 * @author Emilien Huron.
 * @version 1.0
 */
public class PlayerCLI implements PlayerWithLeaderboard {
    
    private GameController gameController;
    private BattlefieldProxy battlefieldProxy;
    private String name;
    private Scanner scanner;

    public PlayerCLI(GameController gameController, BattlefieldProxy battlefieldProxy, String name) {
        this.gameController = gameController;
        this.battlefieldProxy = battlefieldProxy;
        this.name = name;
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void showLeaderboard() {
        Hero hero = battlefieldProxy.getHero();
        System.out.println("\n===== VOS STATISTIQUES PERSONNELLES =====");
        System.out.println("Nom du joueur : " + this.name);
        System.out.println("Points de vie : " + hero.getHealthRemaining() + "/" + hero.getHealthMax());
        System.out.println("Munitions restantes : " + hero.getAmmoRemaining() + "/" + hero.getAmmoMax());
        System.out.println("Boucliers restants : " + hero.getShieldRemaining() + "/" + hero.getShieldMax());
        System.out.println("Mines restantes : " + hero.getMineRemaining() + "/" + hero.getMineMax());
        System.out.println("Bombes restants : " + hero.getBombRemaining() + "/" + hero.getBombMax());
        System.out.println("\n===== " + (hero.isAlive() ? "VOUS AVEZ GAGNÉ ! " : "VOUS AVEZ PERDU ! ") + " =====");
    }

    @Override
    public void takeMyTurn() {
        // Si le héros n'a pas encore été choisi, le joueur doit en sélectionner un.
        if (this.battlefieldProxy.getHero() == null) {
            selectHero();
            return;
        }
        // Si le héros est mort, terminer le tour et ne pas permettre de jouer
        if (this.battlefieldProxy.getHero().getHealthRemaining() <= 0) {
            System.out.println(this.name + " est mort et ne peut plus jouer.");
            return;
        }
        boolean turnInProgress = true;
        while (turnInProgress) {
            System.out.println("\n===== " + this.name + " : C'est votre tour ! =====");
            displayPlayerInfo();
            System.out.println(battlefieldProxy);
            System.out.println("\nChoisissez une action :");
            System.out.println("1. Se déplacer");
            System.out.println("2. Tirer");
            System.out.println("3. Placer une mine");
            System.out.println("4. Placer une bombe");
            System.out.println("5. Activer le bouclier");
            System.out.println("6. Utiliser la hache");
            System.out.println("7. Passer");
            System.out.println("Votre choix : ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        
                        if (moveHero()) {
                            turnInProgress = false;
                        }
                        break;
                    case 2:
                        if (shoot()) {
                            turnInProgress = false;
                        }
                        break;
                    case 3:
                        if (placeExplosive("mine")) {
                            turnInProgress = false;
                        }
                        break;
                    case 4:
                        if (placeExplosive("bomb")) {
                            turnInProgress = false;
                        }
                        break;
                    case 5:
                        if (activateShield()) {
                            turnInProgress = false;
                        }
                        break;
                    case 6:
                        if(ax()) {
                            turnInProgress = false;
                        }
                        break;
                    case 7:
                        turnInProgress = false;
                        break;
                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            }
        }

        // Signaler la fin du tour au contrôleur de jeu pour que le prochain joueur puisse jouer
        synchronized (this.gameController) {
            this.gameController.notifyAll();
        }
    }

    /**
     * Permet au joueur de sélectionner un héros parmi les choix disponibles.
     * Le joueur doit entrer un choix valide pour continuer.
     */
    private void selectHero() {
        boolean heroSelected = false;
        while (!heroSelected) {
            System.out.println("\n===== Sélection de votre Héros =====");
            System.out.println("1. Mastodonte");
            System.out.println("2. Warrior");
            System.out.println("3. Assassin");
            System.out.println("Votre choix : ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        this.battlefieldProxy.addHero("mastodonte");
                        heroSelected = true;
                        break;
                    case 2:
                        this.battlefieldProxy.addHero("warrior");
                        heroSelected = true;
                        break;
                    case 3:
                        this.battlefieldProxy.addHero("assassin");
                        heroSelected = true;
                        break;
                    default:
                        System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            }
        }
    }

    /**
     * Affiche les informations du joueur.
     */
    private void displayPlayerInfo() {
        Hero hero = this.battlefieldProxy.getHero();
        System.out.println("\n===== Informations du Joueur =====");
        System.out.println("Nom du joueur : " + this.name);
        System.out.println("Points de vie : " + hero.getHealthRemaining() + "/" + hero.getHealthMax());
        System.out.println("Munitions restantes : " + hero.getAmmoRemaining() + "/" + hero.getAmmoMax());
        System.out.println("Boucliers restants : " + hero.getShieldRemaining() + "/" + hero.getShieldMax());
        System.out.println("Mines restantes : " + hero.getMineRemaining() + "/" + hero.getMineMax());
        System.out.println("Bombes restants : " + hero.getBombRemaining() + "/" + hero.getBombMax());
        System.out.println("Bouclier actif : " + (hero.isImmune() ? "Oui" : "Non"));
        System.out.println("==================================\n");
    }

    /**
     * Permet au joueur de déplacer son héros dans une direction spécifique.
     *
     * @return true si le déplacement est réussi, false sinon
     */
    private boolean moveHero() {
        System.out.println("Entrez la direction (up, down, left, right) : ");
        String direction = scanner.nextLine();
        if (battlefieldProxy.moveHero(this.battlefieldProxy.getHero(), direction)) {
            System.out.println("Déplacement réussi.");
            return true;
        } else {
            System.out.println("Déplacement impossible.");
            return false;
        }
    }

    /**
     * Permet au joueur de tirer dans une direction spécifique.
     *
     * @return true après avoir tiré
     */
    private boolean shoot() {
        System.out.println("Entrez la direction du tir (up, down, left, right) : ");
        String direction = scanner.nextLine();
        boolean state = battlefieldProxy.shootHero(this.battlefieldProxy.getHero(), direction);
        if (state) {
            System.out.println("Vous avez tiré sur un joueur");
        } else {
            System.out.println("Vous n'avez tiré sur personne");
        }
        return true;
    }

    /**
     * Permet au joueur de placer un explosif à proximité de son héros.
     *
     * @return true si l'explosif est placé avec succès, false sinon.
     */
    private boolean placeExplosive(String explosive) {
        System.out.println("Entrez la direction pour placer la " + explosive + " (up, down, left, right, lu, ru, ld, rd) : ");
        String direction = scanner.nextLine();
        if (battlefieldProxy.addExplosive(this.battlefieldProxy.getHero(), direction, explosive)) {
            System.out.println(explosive+" placée avec succès.");
            return true;
        } else {
            System.out.println("Impossible de placer une" + explosive + "ici.");
            return false;
        }
    }

    /**
     * Permet au joueur d'activer le bouclier de son héros.
     *
     * @return true si le bouclier est activé, false sinon
     */
    private boolean activateShield() {
        if (battlefieldProxy.activateShield(this.battlefieldProxy.getHero())) {
            System.out.println("Bouclier activé.");
            return true;
        } else {
            System.out.println("Impossible d'activer le bouclier.");
            return false;
        }
    }

    /**
     * Permet au joueur de frapper un joueur avec une hache.
     *
     * @return true si le coup de hache est réussi, false sinon
     */
    private boolean ax() {
        System.out.println("Entrez la direction du coup de hache (up, down, left, right) : ");
        String direction = scanner.nextLine();
        boolean state = battlefieldProxy.axAttack(this.battlefieldProxy.getHero(), direction);
        if (state) {
            System.out.println("Vous avez mis un coup de hache sur un joueur !");
        } else {
            System.out.println("Vous n'avez touché personne !");   
        }
        return true;
    }
    
}
