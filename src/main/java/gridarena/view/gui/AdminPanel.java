package gridarena.view.gui;

import gridarena.controller.AdminController;
import gridarena.model.BattlefieldModel;
import gridarena.model.GameConfig;
import gridarena.entity.hero.GroupHeroesToTableModelAdapter;
import gridarena.view.BattlefieldView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Vue principale pour le panneau d'administration de Grid Arena.
 * S'occupe uniquement du rendu graphique Swing et de la capture des valeurs.
 *
 * @author Florian Pépin.
 * @version 2.0
 */
public class AdminPanel extends JFrame {
    
    private final AdminController controller;

    private JSpinner spinGridSize;
    private JSpinner spinWalls;
    private JSpinner spinMedKits;
    private JSpinner spinAmmoKits;
    private JSpinner spinBarrels;
    private JSpinner spinGuiPlayers;
    private JSpinner spinCliPlayers;
    private JSpinner spinBotPlayers;
    private JComboBox<String> comboBotStrategy;
    private JComboBox<String> comboFillStrategy;

    private JButton btnStart;
    private JButton btnStop;

    private JPanel rightPanel;
    private JLabel waitLabel;
    private JTable statsTable;

    public AdminPanel() {
        super("Grid Arena : Panel Admin");
        this.controller = new AdminController(this);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 750);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, new Color(245, 158, 11))); // Amber Border

        // Left Sidebar: Configuration Panel
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(30, 41, 59)); // Slate 800
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(320, 750));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel sideTitle = new JLabel("CONFIGURATION");
        sideTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sideTitle.setForeground(new Color(245, 158, 11)); // Amber
        sideTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(sideTitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        // Config Grid panel
        JPanel configGrid = new JPanel(new GridLayout(5, 2, 5, 10));
        configGrid.setOpaque(false);
        configGrid.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
                "Paramètres Carte",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(241, 245, 249)
        ));

        spinGridSize = createSpinner(10, 5, 30);
        spinWalls = createSpinner(5, 0, 50);
        spinMedKits = createSpinner(3, 0, 50);
        spinAmmoKits = createSpinner(3, 0, 50);
        spinBarrels = createSpinner(3, 0, 50);

        addConfigRow(configGrid, "Taille Grille :", spinGridSize);
        addConfigRow(configGrid, "Murs :", spinWalls);
        addConfigRow(configGrid, "Kits Soin :", spinMedKits);
        addConfigRow(configGrid, "Kits Munitions :", spinAmmoKits);
        addConfigRow(configGrid, "Barils :", spinBarrels);
        sidebar.add(configGrid);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        // Config Players panel
        JPanel configPlayers = new JPanel(new GridLayout(3, 2, 5, 10));
        configPlayers.setOpaque(false);
        configPlayers.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
                "Paramètres Joueurs",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(241, 245, 249)
        ));

        spinGuiPlayers = createSpinner(2, 0, 10);
        spinCliPlayers = createSpinner(0, 0, 10);
        spinBotPlayers = createSpinner(1, 0, 10);

        addConfigRow(configPlayers, "Joueurs GUI :", spinGuiPlayers);
        addConfigRow(configPlayers, "Joueurs CLI :", spinCliPlayers);
        addConfigRow(configPlayers, "Joueurs Bot :", spinBotPlayers);
        sidebar.add(configPlayers);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        // Config Strategies panel
        JPanel configStrats = new JPanel(new GridLayout(2, 2, 5, 10));
        configStrats.setOpaque(false);
        configStrats.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
                "Stratégies",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(241, 245, 249)
        ));

        comboBotStrategy = new JComboBox<>(new String[]{"A* Pathfinding", "Dijkstra", "Greedy BFS"});
        comboFillStrategy = new JComboBox<>(new String[]{"Pattern", "Random", "Modulo"});
        styleComboBox(comboBotStrategy);
        styleComboBox(comboFillStrategy);

        addConfigRow(configStrats, "Strat. Bot :", comboBotStrategy);
        addConfigRow(configStrats, "Placement :", comboFillStrategy);
        sidebar.add(configStrats);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Control Buttons
        btnStart = createStyledButton("Démarrer la partie", new Color(16, 185, 129)); // Green
        btnStart.setForeground(Color.BLACK);
        btnStart.addActionListener(e -> controller.handleStart(getSelectedConfig()));
        sidebar.add(btnStart);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        btnStop = createStyledButton("Arrêter la partie", new Color(244, 63, 94)); // Rose Red
        btnStop.setForeground(Color.WHITE);
        btnStop.setEnabled(false);
        btnStop.addActionListener(e -> controller.handleStop());
        sidebar.add(btnStop);

        this.add(sidebar, BorderLayout.WEST);

        // Right Pane: Live View Panel
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(15, 23, 42)); // Slate 900

        waitLabel = new JLabel("En attente du lancement de la partie...", SwingConstants.CENTER);
        waitLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        waitLabel.setForeground(new Color(148, 163, 184)); // Slate 400
        rightPanel.add(waitLabel, BorderLayout.CENTER);

        this.add(rightPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * Rassemble les valeurs saisies par l'utilisateur dans un conteneur GameConfig.
     *
     * @return l'objet GameConfig correspondant
     */
    public GameConfig getSelectedConfig() {
        return new GameConfig(
                (Integer) spinGridSize.getValue(),
                (Integer) spinWalls.getValue(),
                (Integer) spinMedKits.getValue(),
                (Integer) spinAmmoKits.getValue(),
                (Integer) spinBarrels.getValue(),
                (Integer) spinGuiPlayers.getValue(),
                (Integer) spinCliPlayers.getValue(),
                (Integer) spinBotPlayers.getValue(),
                (String) comboBotStrategy.getSelectedItem(),
                (String) comboFillStrategy.getSelectedItem()
        );
    }

    /**
     * Active/Désactive les composants de configuration en fonction de l'état de la partie.
     *
     * @param isRunning true si la partie tourne, false sinon
     */
    public void setControlState(boolean isRunning) {
        btnStart.setEnabled(!isRunning);
        btnStop.setEnabled(isRunning);
        
        spinGridSize.setEnabled(!isRunning);
        spinWalls.setEnabled(!isRunning);
        spinMedKits.setEnabled(!isRunning);
        spinAmmoKits.setEnabled(!isRunning);
        spinBarrels.setEnabled(!isRunning);
        spinGuiPlayers.setEnabled(!isRunning);
        spinCliPlayers.setEnabled(!isRunning);
        spinBotPlayers.setEnabled(!isRunning);
        comboBotStrategy.setEnabled(!isRunning);
        comboFillStrategy.setEnabled(!isRunning);
    }

    /**
     * Construit et affiche la grille de jeu et le tableau des statistiques globales sur le panneau de droite.
     *
     * @param model le modèle de champ de bataille
     */
    public void displayRunningGame(BattlefieldModel model) {
        rightPanel.removeAll();
        
        statsTable = new JTable(new GroupHeroesToTableModelAdapter(model.getHeroes()));
        statsTable.setBackground(new Color(30, 41, 59));
        statsTable.setForeground(new Color(241, 245, 249));
        statsTable.setGridColor(new Color(51, 65, 85));
        statsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statsTable.setRowHeight(25);
        statsTable.getTableHeader().setBackground(new Color(15, 23, 42));
        statsTable.getTableHeader().setForeground(new Color(245, 158, 11));
        statsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(statsTable);
        scrollPane.setPreferredSize(new Dimension(100, 120));
        scrollPane.getViewport().setBackground(new Color(30, 41, 59));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));
        
        rightPanel.add(scrollPane, BorderLayout.NORTH);
        rightPanel.add(new BattlefieldView(model), BorderLayout.CENTER);
        
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    /**
     * Remet le panneau de droite en mode attente.
     */
    public void displayWaitingScreen() {
        rightPanel.removeAll();
        rightPanel.add(waitLabel, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    /**
     * Force le rafraîchissement visuel du tableau.
     */
    public void refreshStatsTable() {
        if (statsTable != null) {
            statsTable.revalidate();
            statsTable.repaint();
        }
    }

    /**
     * Affiche une boîte de dialogue d'erreur.
     *
     * @param title le titre de l'erreur
     * @param message le corps du message
     */
    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private JSpinner createSpinner(int value, int min, int max) {
        SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, 1);
        JSpinner spinner = new JSpinner(model);
        JComponent editor = spinner.getEditor();
        JFormattedTextField txt = ((JSpinner.DefaultEditor) editor).getTextField();
        txt.setBackground(new Color(15, 23, 42));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        spinner.setBorder(BorderFactory.createLineBorder(new Color(71, 85, 105), 1));
        return spinner;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setBackground(new Color(15, 23, 42));
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBorder(BorderFactory.createLineBorder(new Color(71, 85, 105), 1));
    }

    private void addConfigRow(JPanel panel, String labelText, JComponent comp) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(new Color(203, 213, 225));
        panel.add(label);
        panel.add(comp);
    }

    private JButton createStyledButton(String text, Color baseBg) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg;
                if (!isEnabled()) {
                    bg = new Color(51, 65, 85);
                } else if (getModel().isPressed()) {
                    bg = getBackground().darker();
                } else if (getModel().isRollover()) {
                    bg = getBackground().brighter();
                } else {
                    bg = getBackground();
                }
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                if (!isEnabled()) {
                    g2.setColor(new Color(100, 116, 139));
                } else {
                    g2.setColor(getForeground());
                }
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
                int textX = (getWidth() - stringBounds.width) / 2;
                int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(baseBg);
        button.setMaximumSize(new Dimension(280, 40));
        button.setPreferredSize(new Dimension(280, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}
