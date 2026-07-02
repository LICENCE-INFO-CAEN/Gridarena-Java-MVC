package gridarena.controller;

import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldProxy;
import gridarena.utils.ModelListener;
import gridarena.view.gui.PlayerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Représente le contrôleur permettant à un joueur de faire une action (se déplacer, tirer, etc.).
 * 
 * @author Tom David.
 * @version 1.0
 */
public class ActionController extends JPanel implements ActionListener, ModelListener {
    
    private BattlefieldProxy battlefield;
    private GameController game;
    private PlayerGUI playerGUI;
    private String selectedButton = "Bouger";
    private ArrayList<JButton> actionButtons = new ArrayList<>();
    private ArrayList<JButton> moveButtons = new ArrayList<>();
    private HashMap<String, JLabel> leftAmmos = new HashMap<>();

    public ActionController(BattlefieldProxy battlefield, GameController game, PlayerGUI playerGUI) {
        super(new BorderLayout());
        this.battlefield = battlefield;
        this.battlefield.addModelListener(this);
        this.game = game;
        this.playerGUI = playerGUI;

        this.setPreferredSize(new Dimension(300, 300));
        JPanel topPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        createTopButtons(topPanel, this.battlefield.getHero());
        this.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        createMoveButtons(centerPanel);
        this.add(centerPanel, BorderLayout.CENTER);
        updateButtonStates(selectedButton);
    }

    /**
     * Crée les boutons d'actions.
     *
     * @param panel dans lequel les boutons seront ajoutés.
     * @param hero lié aux events des boutons.
     */
    private void createTopButtons(JPanel panel, Hero hero) {
        HashMap<String, Object> buttonValues = new HashMap<>();
        buttonValues.put("Bouger", "∞");
        buttonValues.put("Tirer", hero.getAmmoRemaining());
        buttonValues.put("Mine", hero.getMineRemaining());
        buttonValues.put("Bombe", hero.getBombRemaining());
        buttonValues.put("Bouclier", hero.getShieldRemaining());
        buttonValues.put("Hache", "∞");
        buttonValues.put("Passer", "∞");

        for (String text : buttonValues.keySet()) {
            JPanel buttonPanel = new JPanel(new BorderLayout());
            JButton button = createButton(text);
            if (text.equals(selectedButton)) {
                button.setBackground(Color.ORANGE);
            }
            actionButtons.add(button);
            JLabel label = new JLabel("Réserve" + ": " + buttonValues.get(text), SwingConstants.CENTER);
            buttonPanel.add(button, BorderLayout.CENTER);
            buttonPanel.add(label, BorderLayout.SOUTH);
            panel.add(buttonPanel);
            leftAmmos.put(text, label);
        }
    }

    /**
     * Crée les boutons de déplacement.
     *
     * @param panel dans lequel les boutons seront ajoutés.
     */
    private void createMoveButtons(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        int[][] positions = {{1, 0}, {2, 1}, {1, 2}, {0, 1}, {0, 0}, {2, 0}, {2, 2}, {0, 2}, {1, 1}};
        String[] directions = {"↑", "→", "↓", "←", "↖", "↗", "↘", "↙", "+"};
        for (int i = 0; i < directions.length; i++) {
            JButton button = createButton(directions[i]);
            gbc.gridx = positions[i][0];
            gbc.gridy = positions[i][1];
            panel.add(button, gbc);
            moveButtons.add(button);
        }
    }

    /**
     * Créer un bouton.
     *
     * @param text à mettre dans le bouton.
     * @return un bouton.
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(50, 50));
        button.addActionListener(this);
        return button;
    }


    /**
     * Met à jour la couleur de fond du panel.
     *
     * @param g graphics.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.GRAY);
    }

    /**
     * Gère les actions du joueur.
     *
     * @param e action effectuée par le joueur.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Hero hero = this.battlefield.getHero();
        JButton sourceButton = (JButton) e.getSource();
        String buttonText = sourceButton.getText();
        if (!playerGUI.isMyTurn()) {
            return;
        }
        boolean valideAction = false;
        if (buttonText.equals("Passer")) {
            valideAction = true;
        } else if (actionButtons.contains(sourceButton)) {
            selectedButton = buttonText;
            updateButtonColors();
            updateButtonStates(buttonText);
            return;
        }
        if (!valideAction) {
            switch (selectedButton) {
                case "Tirer":
                    this.useShoot(buttonText, hero);
                    valideAction = true;
                    break;
                case "Mine":
                    valideAction = this.useExplosive(buttonText, hero, "mine");
                    break;
                case "Bombe":
                    valideAction = this.useExplosive(buttonText, hero, "bomb");
                    break;
                case "Bouclier":
                    valideAction = this.useShield(buttonText, hero);
                    break;
                case "Bouger":
                    valideAction = this.useMove(buttonText, hero);
                    break;
                case "Hache":
                    this.useAx(buttonText, hero);
                    valideAction = true;
                    break;
                default:
                    throw new IllegalArgumentException("Action invalide.");
            }
        }
        if (valideAction) {
            playerGUI.setMyTurn(false);
            synchronized (this.game) {
                game.notifyAll();
            }
            updateButtonEnabled();
        }
    }
    /**
     * Met à jour la couleur des boutons d'actions.
     */
    private void updateButtonColors() {
        for (JButton button : actionButtons) {
            if (button.getText().equals(selectedButton)) {
                button.setBackground(Color.ORANGE);
            } else {
                button.setBackground(null);
            }
        }
    }

    /**
     * Met à jour les boutons d'actions.
     */
    private void updateButtonEnabled() {
        for (JButton button : actionButtons) {
            if (leftAmmos.get(button.getText()).getText().equals("Réserve: 0")) {
                if (button.getText().equals(selectedButton)) {
                    selectedButton = "Bouger";
                    updateButtonColors();
                    updateButtonStates(selectedButton);
                }
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }
        }
    }

    /**
     * Met à jour les boutons de déplacement.
     *
     * @param buttonText action sélectionnée.
     */
    private void updateButtonStates(String buttonText) {
        String[] validDirections;
        switch (buttonText) {
            case "Bouger":
            case "Hache":
            case "Tirer":
                if (leftAmmos.get(buttonText).getText().equals("Réserve: 0")) {
                    validDirections = new String[]{};
                    break;
                }
                validDirections = new String[]{"↑", "→", "↓", "←"};
                break;
            case "Mine":
            case "Bombe":
                if (leftAmmos.get(buttonText).getText().equals("Réserve: 0")) {
                    validDirections = new String[]{};
                    break;
                }
                validDirections = new String[]{"↑", "→", "↓", "←", "↖", "↗", "↘", "↙"};
                break;
            case "Bouclier":
                if (leftAmmos.get(buttonText).getText().equals("Réserve: 0")) {
                    validDirections = new String[]{};
                    break;
                }
                validDirections = new String[]{"+"};
                break;
            case "Passer":
                validDirections = new String[]{};
                break;
            default:
                validDirections = new String[]{};
        }
        setMoveButtonsEnabled(validDirections);
    }

    /**
     * Met à jour les boutons de déplacement.
     *
     * @param validDirections directions valides pour le déplacement.
     */
    private void setMoveButtonsEnabled(String[] validDirections) {
        for (JButton button : moveButtons) {
            button.setEnabled(false);
            for (String direction : validDirections) {
                if (button.getText().equals(direction)) {
                    button.setEnabled(true);
                }
            }
        }
    }


    /**
     * Utiliser le déplacement du joueur.
     *
     * @param direction dans laquelle le joueur veut se déplacer.
     * @param hero qui souhaite se déplacer.
     * @return true si le déplacement a été effectué sinon false.
     */
    private boolean useMove(String direction, Hero hero) {
        switch (direction) {
            case "↑":
                return this.battlefield.moveHero(hero, "up");
            case "→":
                return this.battlefield.moveHero(hero, "right");
            case "↓":
                return this.battlefield.moveHero(hero, "down");
            case "←":
                return this.battlefield.moveHero(hero, "left");
            default:
                return false;
        }
    }


    /**
     * Utiliser le tir du joueur.
     *
     * @param direction dans laquelle le joueur veut tirer.
     * @param hero qui souhaite tirer.
     */
    private void useShoot(String direction, Hero hero) {
        switch (direction) {
            case "↑":
                this.battlefield.shootHero(hero, "up");
                break;
            case "→":
                this.battlefield.shootHero(hero, "right");
                break;
            case "↓":
                this.battlefield.shootHero(hero, "down");
                break;
            case "←":
                this.battlefield.shootHero(hero, "left");
                break;
        }
    }


    /**
     * Utiliser un explosif.
     *
     * @param direction dans laquelle le joueur veut poser un explosif.
     * @param hero qui souhaite poser un explosif.
     * @param explosive a poser.
     * @return true si l'explosif a pu être posé sinon false.
     */

    private boolean useExplosive(String direction, Hero hero, String explosive) {
        switch (direction) {
            case "↑":
                return this.battlefield.addExplosive(hero, "up", explosive);
            case "→":
                return this.battlefield.addExplosive(hero, "right", explosive);
            case "↓":
                return this.battlefield.addExplosive(hero, "down", explosive);
            case "←":
                return this.battlefield.addExplosive(hero, "left", explosive);
            case "↖":
                return this.battlefield.addExplosive(hero, "lu", explosive);
            case "↗":
                return this.battlefield.addExplosive(hero, "ru", explosive);
            case "↘":
                return this.battlefield.addExplosive(hero, "rd", explosive);
            case "↙":
                return this.battlefield.addExplosive(hero, "ld", explosive);
            default:
                return true;
        }
    }

/**
     * Utiliser un coup de hache.
     *
     * @param direction dans laquelle le joueur veut frapper.
     * @param hero qui souhaite utiliser la hache.
     */
    private void useAx(String direction, Hero hero) {
        switch (direction) {
            case "↑":
                this.battlefield.axAttack(hero, "up");
                break;
            case "→":
                this.battlefield.axAttack(hero, "right");
                break;
            case "↓":
                this.battlefield.axAttack(hero, "down");
                break;
            case "←":
                this.battlefield.axAttack(hero, "left");
                break;
        }
    }

    /**
     * Activer le bouclier du joueur.
     *
     * @param direction du bouclier.
     * @param hero qui souhaite activer le bouclier.
     * @return true si le bouclier a été activé sinon false.
     */
    private boolean useShield(String direction, Hero hero) {
        switch (direction) {
            case "+":
                return this.battlefield.activateShield(hero);
            default:
                return true;
        }
    }
    /**
     * Met à jour les JLabels pour chaque action.
     */
    @Override
    public void updatedModel(Object source) {
        Hero hero = this.battlefield.getHero();
        leftAmmos.get("Mine").setText("Réserve: " + hero.getMineRemaining());
        leftAmmos.get("Bombe").setText("Réserve: " + hero.getBombRemaining());
        leftAmmos.get("Bouclier").setText("Réserve: " + hero.getShieldRemaining());
        leftAmmos.get("Tirer").setText("Réserve: " + hero.getAmmoRemaining());
    }

}