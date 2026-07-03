package gridarena.view.gui;

import gridarena.controller.AdminController;
import gridarena.entity.hero.GroupHeroesToTableModelAdapter;
import gridarena.model.BattlefieldModel;
import gridarena.model.GameConfig;
import gridarena.view.BattlefieldView;
import gridarena.view.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Vue principale du panneau d'administration de Grid Arena.
 * Thème : "Tactical HQ" — charcoal chaud, accent crimson, angles droits.
 *
 * @author Florian Pépin.
 * @version 3.0
 */
public class AdminPanel extends JFrame {

    private final AdminController controller;

    private JSpinner spinGridSize;
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
        super("Panel Admin");
        this.controller = new AdminController(this);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1050, 760);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(UITheme.BG_PRIMARY);

        // ── Barre de titre custom ──────────────────────────────────────────────
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(UITheme.ACCENT);
        titleBar.setPreferredSize(new Dimension(0, 36));
        JLabel titleLabel = new JLabel("  ▮  PANEL ADMIN");
        titleLabel.setFont(UITheme.FONT_BADGE);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleBar.add(titleLabel, BorderLayout.WEST);
        this.add(titleBar, BorderLayout.NORTH);

        // ── Sidebar gauche ─────────────────────────────────────────────────────
        JPanel sidebar = buildSidebar();
        this.add(sidebar, BorderLayout.WEST);

        // ── Zone de jeu droite ────────────────────────────────────────────────
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(UITheme.BG_PRIMARY);

        waitLabel = new JLabel("EN ATTENTE DU LANCEMENT", SwingConstants.CENTER);
        waitLabel.setFont(UITheme.FONT_TITLE);
        waitLabel.setForeground(UITheme.TEXT_MUTED);
        rightPanel.add(waitLabel, BorderLayout.CENTER);

        this.add(rightPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    // ── Construction de la sidebar ─────────────────────────────────────────────

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Bordure droite séparatrice
                g.setColor(UITheme.ACCENT);
                g.fillRect(getWidth() - 3, 0, 3, getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(310, 0));
        sidebar.setBackground(UITheme.BG_SECONDARY);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(18, 16, 18, 20));

        // Section Carte
        spinGridSize = createSpinner(10, 5, 30);

        JPanel gridSection = createGridSection(2, new JComponent[][]{
            {makeLabel("Taille grille"), spinGridSize}
        });
        sidebar.add(makeBadge("PARAMÈTRES CARTE"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(gridSection);
        sidebar.add(Box.createRigidArea(new Dimension(0, 16)));

        // Section Joueurs
        spinGuiPlayers = createSpinner(2, 0, 10);
        spinCliPlayers = createSpinner(0, 0, 10);
        spinBotPlayers = createSpinner(1, 0, 10);

        JPanel playersSection = createGridSection(2, new JComponent[][]{
            {makeLabel("Joueurs GUI"),  spinGuiPlayers},
            {makeLabel("Joueurs CLI"),  spinCliPlayers},
            {makeLabel("Robots"),       spinBotPlayers}
        });
        sidebar.add(makeBadge("JOUEURS"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(playersSection);
        sidebar.add(Box.createRigidArea(new Dimension(0, 16)));

        // Section Stratégies
        comboBotStrategy  = createCombo("A* Pathfinding", "Dijkstra", "Greedy BFS");
        comboFillStrategy = createCombo("Pattern - Cross", "Pattern - Pillars", "Pattern - Rings");

        JPanel stratSection = createGridSection(2, new JComponent[][]{
            {makeLabel("Strat. bot"),   comboBotStrategy},
            {makeLabel("Placement"),    comboFillStrategy}
        });
        sidebar.add(makeBadge("STRATÉGIES"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(stratSection);
        sidebar.add(Box.createRigidArea(new Dimension(0, 24)));

        // Séparateur
        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BORDER);
        sep.setBackground(UITheme.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(sep);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Boutons
        btnStart = createPrimaryButton("LANCER LA PARTIE");
        btnStart.addActionListener(e -> controller.handleStart(getSelectedConfig()));
        sidebar.add(btnStart);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        btnStop = createDangerButton("ARRÊTER LA PARTIE");
        btnStop.setEnabled(false);
        btnStop.addActionListener(e -> controller.handleStop());
        sidebar.add(btnStop);

        // Légende raccourcis clavier
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(makeHint("1–6 : mode action  |  Flèches / ZQSD : action  |  P : passer"));

        return sidebar;
    }

    // ── Widgets helpers ────────────────────────────────────────────────────────

    private JLabel makeBadge(String text) {
        JLabel badge = new JLabel("  " + text + "  ") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(UITheme.ACCENT);
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        badge.setFont(UITheme.FONT_BADGE);
        badge.setForeground(UITheme.TEXT_PRIMARY);
        badge.setOpaque(false);
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        badge.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        return badge;
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_LABEL);
        l.setForeground(UITheme.TEXT_SECONDARY);
        return l;
    }

    private JLabel makeHint(String text) {
        JLabel hint = new JLabel(text);
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        hint.setForeground(UITheme.TEXT_MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        return hint;
    }

    private JPanel createGridSection(int cols, JComponent[][] rows) {
        JPanel panel = new JPanel(new GridLayout(rows.length, cols, 6, 8));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rows.length * 34));
        for (JComponent[] row : rows) {
            for (JComponent c : row) panel.add(c);
        }
        return panel;
    }

    private JSpinner createSpinner(int value, int min, int max) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, 1));
        JFormattedTextField tf = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        tf.setBackground(UITheme.BG_TERTIARY);
        tf.setForeground(UITheme.TEXT_PRIMARY);
        tf.setCaretColor(UITheme.TEXT_PRIMARY);
        tf.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        spinner.setBackground(UITheme.BG_TERTIARY);
        spinner.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        return spinner;
    }

    private JComboBox<String> createCombo(String... items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(UITheme.BG_TERTIARY);
        combo.setForeground(UITheme.TEXT_PRIMARY);
        combo.setFont(UITheme.FONT_BODY);
        combo.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        return combo;
    }

    /** Bouton principal rouge plein, angles droits. */
    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg;
                if      (!isEnabled())           bg = UITheme.BG_TERTIARY;
                else if (getModel().isPressed())  bg = UITheme.ACCENT.darker();
                else if (getModel().isRollover()) bg = UITheme.ACCENT_HOVER;
                else                              bg = UITheme.ACCENT;
                g2.setColor(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(isEnabled() ? UITheme.TEXT_PRIMARY : UITheme.TEXT_MUTED);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        styleButton(btn);
        return btn;
    }

    /** Bouton secondaire : fond sombre avec bordure crimson basse, texte rouge. */
    private JButton createDangerButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color bg;
                if      (!isEnabled())           bg = UITheme.BG_TERTIARY;
                else if (getModel().isPressed())  bg = new Color(0x3A, 0x10, 0x10);
                else if (getModel().isRollover()) bg = new Color(0x35, 0x18, 0x18);
                else                              bg = UITheme.BG_TERTIARY;
                g2.setColor(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Bordure basse crimson
                g2.setColor(isEnabled() ? UITheme.ACCENT : UITheme.TEXT_MUTED);
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        styleButton(btn);
        btn.setForeground(UITheme.ACCENT);
        return btn;
    }

    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setFont(UITheme.FONT_LABEL);
        btn.setForeground(UITheme.TEXT_PRIMARY);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setPreferredSize(new Dimension(270, 38));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // ── API publique (appelée par AdminController) ─────────────────────────────

    public GameConfig getSelectedConfig() {
        return new GameConfig(
                (Integer) spinGridSize.getValue(),
                0,
                0,
                0,
                0,
                (Integer) spinGuiPlayers.getValue(),
                (Integer) spinCliPlayers.getValue(),
                (Integer) spinBotPlayers.getValue(),
                (String) comboBotStrategy.getSelectedItem(),
                (String) comboFillStrategy.getSelectedItem()
        );
    }

    public void setControlState(boolean isRunning) {
        btnStart.setEnabled(!isRunning);
        btnStop.setEnabled(isRunning);
        spinGridSize.setEnabled(!isRunning);
        spinGuiPlayers.setEnabled(!isRunning);
        spinCliPlayers.setEnabled(!isRunning);
        spinBotPlayers.setEnabled(!isRunning);
        comboBotStrategy.setEnabled(!isRunning);
        comboFillStrategy.setEnabled(!isRunning);
    }

    public void displayRunningGame(BattlefieldModel model) {
        rightPanel.removeAll();

        // Header live
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.BG_SECONDARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UITheme.ACCENT));
        JLabel liveLabel = new JLabel("  ● LIVE");
        liveLabel.setFont(UITheme.FONT_BADGE);
        liveLabel.setForeground(UITheme.ACCENT);
        header.add(liveLabel, BorderLayout.WEST);
        rightPanel.add(header, BorderLayout.NORTH);

        // Tableau stats
        statsTable = new JTable(new GroupHeroesToTableModelAdapter(model.getHeroes()));
        statsTable.setBackground(UITheme.BG_SECONDARY);
        statsTable.setForeground(UITheme.TEXT_PRIMARY);
        statsTable.setGridColor(UITheme.BORDER);
        statsTable.setFont(UITheme.FONT_BODY);
        statsTable.setRowHeight(24);
        statsTable.setShowHorizontalLines(true);
        statsTable.setShowVerticalLines(false);
        
        // Centrage du texte des cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < statsTable.getColumnCount(); i++) {
            statsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Centrage et stylisation des entêtes de colonnes
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(UITheme.ACCENT);
                setForeground(UITheme.TEXT_PRIMARY);
                setFont(UITheme.FONT_LABEL);
                return this;
            }
        };
        statsTable.getTableHeader().setDefaultRenderer(headerRenderer);

        JScrollPane scroll = new JScrollPane(statsTable);
        scroll.setPreferredSize(new Dimension(0, 110));
        scroll.getViewport().setBackground(UITheme.BG_SECONDARY);
        scroll.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UITheme.BORDER));

        rightPanel.add(scroll, BorderLayout.SOUTH);
        rightPanel.add(new BattlefieldView(model), BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public void displayWaitingScreen() {
        rightPanel.removeAll();
        rightPanel.add(waitLabel, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public void refreshStatsTable() {
        if (statsTable != null) {
            statsTable.revalidate();
            statsTable.repaint();
        }
    }

    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
