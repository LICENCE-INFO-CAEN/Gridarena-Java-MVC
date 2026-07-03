package gridarena.controller.cli;

import gridarena.controller.GameController;
import gridarena.controller.Player;
import gridarena.controller.command.*;
import gridarena.entity.hero.*;
import gridarena.model.*;

import java.util.Scanner;

/**
 * Représente l'interface utilisateur d'un joueur en ligne de commandes (Contrôleur).
 *
 * @author Emilien Huron, Florian Pépin.
 * @version 2.0
 */
public class PlayerCLIController implements Player {
    
    private GameController gameController;
    private BattlefieldModel battlefieldProxy;
    private String name;
    private Scanner scanner;

    public PlayerCLIController(GameController gameController, BattlefieldModel battlefieldProxy, String name) {
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
        Hero hero = battlefieldProxy.getCurrentHero();
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
        if (this.battlefieldProxy.getCurrentHero() == null) {
            selectHero();
            return;
        }
        // Si le héros est mort, terminer le tour et ne pas permettre de jouer
        if (this.battlefieldProxy.getCurrentHero().getHealthRemaining() <= 0) {
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
                Command cmd = null;
                switch (choice) {
                    case 1:
                        cmd = moveHero();
                        if (cmd != null) {
                            if (cmd.execute()) {
                                System.out.println("Déplacement réussi.");
                                turnInProgress = false;
                            } else {
                                System.out.println("Déplacement impossible.");
                            }
                        }
                        break;
                    case 2:
                        cmd = shoot();
                        if (cmd != null) {
                            cmd.execute();
                            System.out.println("Tir effectué.");
                            turnInProgress = false;
                        }
                        break;
                    case 3:
                        cmd = placeExplosive("mine");
                        if (cmd != null) {
                            if (cmd.execute()) {
                                System.out.println("Mine placée avec succès.");
                                turnInProgress = false;
                            } else {
                                System.out.println("Impossible de placer une mine ici.");
                            }
                        }
                        break;
                    case 4:
                        cmd = placeExplosive("bomb");
                        if (cmd != null) {
                            if (cmd.execute()) {
                                System.out.println("Bombe placée avec succès.");
                                turnInProgress = false;
                            } else {
                                System.out.println("Impossible de placer une bombe ici.");
                            }
                        }
                        break;
                    case 5:
                        cmd = activateShield();
                        if (cmd != null) {
                            if (cmd.execute()) {
                                System.out.println("Bouclier activé.");
                                turnInProgress = false;
                            } else {
                                System.out.println("Impossible d'activer le bouclier.");
                            }
                        }
                        break;
                    case 6:
                        cmd = ax();
                        if (cmd != null) {
                            cmd.execute();
                            System.out.println("Coup de hache effectué.");
                            turnInProgress = false;
                        }
                        break;
                    case 7:
                        cmd = new PassCommand();
                        cmd.execute();
                        turnInProgress = false;
                        break;
                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            }
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
            System.out.println("4. Personnalisé (Répartir 10 points)");
            System.out.println("Votre choix : ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        this.battlefieldProxy.addHero(HeroFactory.getFactory("mastodonte"));
                        heroSelected = true;
                        break;
                    case 2:
                        this.battlefieldProxy.addHero(HeroFactory.getFactory("warrior"));
                        heroSelected = true;
                        break;
                    case 3:
                        this.battlefieldProxy.addHero(HeroFactory.getFactory("assassin"));
                        heroSelected = true;
                        break;
                    case 4:
                        System.out.println("\n--- Personnalisation de votre Héros ---");
                        System.out.println("Vous disposez de 10 points à distribuer.");
                        int pointsLeft = 10;
                        int ptsHp = 0, ptsAmmo = 0, ptsShield = 0, ptsMine = 0, ptsBomb = 0;
                        
                        System.out.println("Points pour la Vie (base 50, +10 PV/point) - restants : " + pointsLeft);
                        ptsHp = Integer.parseInt(scanner.nextLine());
                        if (ptsHp < 0 || ptsHp > pointsLeft) { System.out.println("Saisie invalide."); break; }
                        pointsLeft -= ptsHp;

                        System.out.println("Points pour les Munitions (base 2, +2 munitions/point) - restants : " + pointsLeft);
                        ptsAmmo = Integer.parseInt(scanner.nextLine());
                        if (ptsAmmo < 0 || ptsAmmo > pointsLeft) { System.out.println("Saisie invalide."); break; }
                        pointsLeft -= ptsAmmo;

                        System.out.println("Points pour les Boucliers (base 0, +1 bouclier/point) - restants : " + pointsLeft);
                        ptsShield = Integer.parseInt(scanner.nextLine());
                        if (ptsShield < 0 || ptsShield > pointsLeft) { System.out.println("Saisie invalide."); break; }
                        pointsLeft -= ptsShield;

                        System.out.println("Points pour les Mines (base 0, +1 mine/point) - restants : " + pointsLeft);
                        ptsMine = Integer.parseInt(scanner.nextLine());
                        if (ptsMine < 0 || ptsMine > pointsLeft) { System.out.println("Saisie invalide."); break; }
                        pointsLeft -= ptsMine;

                        System.out.println("Points pour les Bombes (base 0, +1 bombe/point) - restants : " + pointsLeft);
                        ptsBomb = Integer.parseInt(scanner.nextLine());
                        if (ptsBomb < 0 || ptsBomb > pointsLeft) { System.out.println("Saisie invalide."); break; }
                        pointsLeft -= ptsBomb;

                        int maxHealth = 50 + ptsHp * 10;
                        int maxAmmo = 2 + ptsAmmo * 2;
                        int maxShield = ptsShield;
                        int maxMine = ptsMine;
                        int maxBomb = ptsBomb;

                        this.battlefieldProxy.addHero(new CustomHeroFactory(maxHealth, maxAmmo, maxShield, maxMine, maxBomb));
                        heroSelected = true;
                        System.out.println("Héros personnalisé créé (PV: " + maxHealth + ", Munitions: " + maxAmmo + ", Boucliers: " + maxShield + ", Mines: " + maxMine + ", Bombes: " + maxBomb + ")");
                        break;
                    default:
                        System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erreur : veuillez saisir un nombre entier valide.");
            }
        }
    }

    /**
     * Affiche les informations du joueur.
     */
    private void displayPlayerInfo() {
        Hero hero = this.battlefieldProxy.getCurrentHero();
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
     * @return Commande de déplacement
     */
    private Command moveHero() {
        System.out.println("Entrez la direction (up, down, left, right) : ");
        String direction = scanner.nextLine();
        return new MoveCommand(battlefieldProxy, this.battlefieldProxy.getCurrentHero(), direction);
    }

    /**
     * Permet au joueur de tirer dans une direction spécifique.
     *
     * @return Commande de tir
     */
    private Command shoot() {
        System.out.println("Entrez la direction du tir (up, down, left, right) : ");
        String direction = scanner.nextLine();
        return new ShootCommand(battlefieldProxy, this.battlefieldProxy.getCurrentHero(), direction);
    }

    /**
     * Permet au joueur de placer un explosif à proximité de son héros.
     *
     * @return Commande de dépôt d'explosif
     */
    private Command placeExplosive(String explosive) {
        System.out.println("Entrez la direction pour placer la " + explosive + " (up, down, left, right, lu, ru, ld, rd) : ");
        String direction = scanner.nextLine();
        return new AddExplosiveCommand(battlefieldProxy, this.battlefieldProxy.getCurrentHero(), direction, explosive);
    }

    /**
     * Permet au joueur d'activer le bouclier de son héros.
     *
     * @return Commande d'activation du bouclier
     */
    private Command activateShield() {
        return new ActivateShieldCommand(battlefieldProxy, this.battlefieldProxy.getCurrentHero());
    }

    /**
     * Permet au joueur de frapper un joueur avec une hache.
     *
     * @return Commande d'attaque à la hache
     */
    private Command ax() {
        System.out.println("Entrez la direction du coup de hache (up, down, left, right) : ");
        String direction = scanner.nextLine();
        return new AxAttackCommand(battlefieldProxy, this.battlefieldProxy.getCurrentHero(), direction);
    }

    @Override
    public boolean hasHero() {
        return this.battlefieldProxy.getCurrentHero() != null;
    }
}
