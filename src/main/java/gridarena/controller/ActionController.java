package gridarena.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import gridarena.entity.hero.Hero;
import gridarena.model.BattlefieldModel;
import gridarena.utils.ModelListener;
import gridarena.view.UITheme;
import gridarena.view.gui.PlayerGUI;
import gridarena.controller.command.*;
import gridarena.controller.state.*;
import java.awt.event.KeyEvent;

/**
 * Représente le contrôleur permettant à un joueur de faire une action (se déplacer, tirer, etc.).
 * 
 * @author Tom David, Florian Pépin.
 * @version 2.0
 */
public class ActionController extends JPanel implements ActionListener, ModelListener {
    
    private BattlefieldModel battlefield;
    private GameController game;
    private PlayerGUI playerGUI;
    private String selectedButton = "Bouger";
    private ControllerState currentState = new MoveState();
    private ArrayList<JButton> actionButtons = new ArrayList<>();
    private ArrayList<JButton> moveButtons = new ArrayList<>();
    private HashMap<String, JLabel> leftAmmos = new HashMap<>();

    public ActionController(BattlefieldModel battlefield, GameController game, PlayerGUI playerGUI) {
        super(new BorderLayout());
        this.battlefield = battlefield;
        this.battlefield.addModelListener(this);
        this.game = game;
        this.playerGUI = playerGUI;

        this.setPreferredSize(new Dimension(300, 300));
        JPanel topPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        topPanel.setOpaque(false);
        createTopButtons(topPanel, this.battlefield.getCurrentHero());
        this.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        createMoveButtons(centerPanel);
        this.add(centerPanel, BorderLayout.CENTER);
        updateButtonStates(selectedButton);
        
        this.registerKeyBindings();
    }

    private void createTopButtons(JPanel panel, Hero hero) {
        HashMap<String, Object> buttonValues = new HashMap<>();
        buttonValues.put("Bouger",  "∞");
        buttonValues.put("Tirer",   hero.getAmmoRemaining());
        buttonValues.put("Mine",    hero.getMineRemaining());
        buttonValues.put("Bombe",   hero.getBombRemaining());
        buttonValues.put("Bouclier",hero.getShieldRemaining());
        buttonValues.put("Hache",   "∞");
        buttonValues.put("Passer",  "∞");

        for (String text : buttonValues.keySet()) {
            JPanel buttonPanel = new JPanel(new BorderLayout(0, 2));
            buttonPanel.setOpaque(false);
            JButton button = createButton(text);
            if (text.equals(selectedButton)) {
                button.setBackground(UITheme.ACCENT);
                button.setForeground(UITheme.TEXT_PRIMARY);
            }
            actionButtons.add(button);
            JLabel label = new JLabel("" + buttonValues.get(text), SwingConstants.CENTER);
            label.setForeground(UITheme.TEXT_MUTED);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            buttonPanel.add(button, BorderLayout.CENTER);
            buttonPanel.add(label, BorderLayout.SOUTH);
            panel.add(buttonPanel);
            leftAmmos.put(text, label);
        }
    }

    private void createMoveButtons(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 8;
        gbc.ipady = 8;
        int[][] positions = {{1, 0}, {2, 1}, {1, 2}, {0, 1}, {0, 0}, {2, 0}, {2, 2}, {0, 2}, {1, 1}};
        String[] directions = {"↑", "→", "↓", "←", "↖", "↗", "↘", "↙", "+"};
        for (int i = 0; i < directions.length; i++) {
            JButton button = createDirectionButton(directions[i]);
            gbc.gridx = positions[i][0];
            gbc.gridy = positions[i][1];
            panel.add(button, gbc);
            moveButtons.add(button);
        }
    }

    /** Bouton d'action (mode) : fond charcoal, crimson quand actif. */
    private JButton createButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color bg;
                if      (!isEnabled())           bg = new Color(0x22, 0x22, 0x24);
                else if (getModel().isPressed())  bg = getBackground().darker();
                else if (getModel().isRollover()) bg = getBackground().brighter();
                else                              bg = getBackground();
                g2.setColor(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Trait d'accentuation gauche pour le bouton actif
                if (isEnabled() && getBackground().equals(UITheme.ACCENT)) {
                    g2.setColor(UITheme.ACCENT_HOVER);
                    g2.fillRect(0, 0, 3, getHeight());
                }
                g2.setColor(isEnabled() ? getForeground() : UITheme.TEXT_MUTED);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth()  - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFont(UITheme.FONT_LABEL);
        button.setForeground(UITheme.TEXT_PRIMARY);
        button.setBackground(UITheme.BG_TERTIARY);
        button.addActionListener(this);
        return button;
    }

    /** Bouton de direction : fond sombre, barre crimson basse au survol. */
    private JButton createDirectionButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color bg;
                if      (!isEnabled())           bg = new Color(0x1A, 0x1A, 0x1C);
                else if (getModel().isPressed())  bg = UITheme.ACCENT;
                else if (getModel().isRollover()) bg = UITheme.BG_SECONDARY;
                else                              bg = UITheme.BG_PRIMARY;
                g2.setColor(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                if (isEnabled() && getModel().isRollover()) {
                    g2.setColor(UITheme.ACCENT);
                    g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                }
                g2.setColor(isEnabled() ? UITheme.TEXT_PRIMARY : UITheme.TEXT_MUTED);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth()  - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(UITheme.TEXT_PRIMARY);
        button.setBackground(UITheme.BG_PRIMARY);
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
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(UITheme.BG_SECONDARY);
        g2.fillRect(0, 0, getWidth(), getHeight());
        // Bordure droite séparatrice
        g2.setColor(UITheme.ACCENT);
        g2.fillRect(getWidth() - 2, 0, 2, getHeight());
        g2.dispose();
    }

    /**
     * Gère les actions du joueur.
     *
     * @param e action effectuée par le joueur.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();
        String buttonText = sourceButton.getText();
        if (!playerGUI.isMyTurn()) {
            return;
        }
        
        if (buttonText.equals("Passer")) {
            executeCommand(new PassCommand());
            return;
        }
        
        // Clic sur un bouton d'action principal
        for (JButton btn : actionButtons) {
            if (btn == sourceButton) {
                selectActionByName(buttonText);
                return;
            }
        }
        
        // Sinon, c'est un bouton de direction
        String dir = getDirectionFromSymbol(buttonText);
        triggerDirection(dir);
    }

    private void selectActionByName(String name) {
        selectedButton = name;
        switch (name) {
            case "Bouger":
                currentState = new MoveState();
                break;
            case "Tirer":
                currentState = new ShootState();
                break;
            case "Mine":
                currentState = new MineState();
                break;
            case "Bombe":
                currentState = new BombState();
                break;
            case "Bouclier":
                currentState = new ShieldState();
                break;
            case "Hache":
                currentState = new AxAttackState();
                break;
        }
        updateButtonColors();
        updateButtonStates(name);
    }

    private void triggerDirection(String direction) {
        Hero hero = this.battlefield.getCurrentHero();
        Command cmd = this.currentState.getCommandForDirection(this.battlefield, hero, direction);
        executeCommand(cmd);
    }

    private void executeCommand(Command cmd) {
        if (cmd != null && cmd.execute()) {
            playerGUI.setMyTurn(false);
            playerGUI.signalTurnDone();
            updateButtonEnabled();
        }
    }

    private void registerKeyBindings() {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        // Raccourcis clavier (touches 1 à 6 pour changer de mode)
        String[] actions = {"Bouger", "Tirer", "Mine", "Bombe", "Bouclier", "Hache"};
        int[] keyCodes = {KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6};
        for (int i = 0; i < actions.length; i++) {
            final String actionName = actions[i];
            inputMap.put(KeyStroke.getKeyStroke(keyCodes[i], 0), "select_" + actionName);
            actionMap.put("select_" + actionName, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (playerGUI.isMyTurn()) {
                        selectActionByName(actionName);
                    }
                }
            });
        }
        
        // Passer son tour avec 'P' ou 'Entrée'
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pass_turn");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "pass_turn");
        actionMap.put("pass_turn", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerGUI.isMyTurn()) {
                    executeCommand(new PassCommand());
                }
            }
        });

        // Touches de direction (Flèches et ZQSD/WASD)
        String[] directions = {"up", "right", "down", "left"};
        int[][] directionKeys = {
            {KeyEvent.VK_UP, KeyEvent.VK_Z, KeyEvent.VK_W},
            {KeyEvent.VK_RIGHT, KeyEvent.VK_D, KeyEvent.VK_D},
            {KeyEvent.VK_DOWN, KeyEvent.VK_S, KeyEvent.VK_S},
            {KeyEvent.VK_LEFT, KeyEvent.VK_Q, KeyEvent.VK_A}
        };

        for (int i = 0; i < directions.length; i++) {
            final String dir = directions[i];
            for (int keyCode : directionKeys[i]) {
                String actionKey = "press_" + dir + "_" + keyCode;
                inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), actionKey);
                actionMap.put(actionKey, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (playerGUI.isMyTurn()) {
                            triggerDirection(dir);
                        }
                    }
                });
            }
        }
    }

    /**
     * Met à jour la couleur des boutons d'actions.
     */
    private void updateButtonColors() {
        for (JButton button : actionButtons) {
            if (button.getText().equals(selectedButton)) {
                button.setBackground(UITheme.ACCENT);
                button.setForeground(UITheme.TEXT_PRIMARY);
            } else {
                button.setBackground(UITheme.BG_TERTIARY);
                button.setForeground(UITheme.TEXT_SECONDARY);
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
     * Traduit le symbole de direction Swing en chaîne compréhensible par le modèle.
     */
    private String getDirectionFromSymbol(String symbol) {
        switch (symbol) {
            case "↑": return "up";
            case "→": return "right";
            case "↓": return "down";
            case "←": return "left";
            case "↖": return "lu";
            case "↗": return "ru";
            case "↘": return "rd";
            case "↙": return "ld";
            default: return "";
        }
    }
    /**
     * Met à jour les JLabels pour chaque action.
     */
    @Override
    public void updatedModel(Object source) {
        Hero hero = this.battlefield.getCurrentHero();
        leftAmmos.get("Mine").setText("Réserve: " + hero.getMineRemaining());
        leftAmmos.get("Bombe").setText("Réserve: " + hero.getBombRemaining());
        leftAmmos.get("Bouclier").setText("Réserve: " + hero.getShieldRemaining());
        leftAmmos.get("Tirer").setText("Réserve: " + hero.getAmmoRemaining());
    }

}