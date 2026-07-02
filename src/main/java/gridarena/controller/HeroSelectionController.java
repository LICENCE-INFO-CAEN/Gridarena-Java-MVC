package gridarena.controller;

import gridarena.model.BattlefieldModel;
import gridarena.view.gui.PlayerGUI;
import gridarena.entity.hero.HeroFactory;
import gridarena.entity.hero.CustomHeroFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Représente le contrôleur permettant de sélectionner ou de personnaliser son Hero au début de la partie.
 * 
 * @author Emilien Huron et Florian Pépin.
 * @version 3.0
 */
public class HeroSelectionController extends JPanel {
    
    private BattlefieldModel battlefield;
    private GameController game;
    private PlayerGUI playerGUI;

    private int pointsLeft = 10;
    private int ptsHp = 0;
    private int ptsAmmo = 0;
    private int ptsShield = 0;
    private int ptsMine = 0;
    private int ptsBomb = 0;

    private JLabel labelPointsLeft;
    private JLabel labelHp, labelAmmo, labelShield, labelMine, labelBomb;
    private JButton btnHpPlus, btnHpMinus;
    private JButton btnAmmoPlus, btnAmmoMinus;
    private JButton btnShieldPlus, btnShieldMinus;
    private JButton btnMinePlus, btnMineMinus;
    private JButton btnBombPlus, btnBombMinus;
    
    public HeroSelectionController(BattlefieldModel battlefield, GameController game, PlayerGUI playerGUI) {
        super(new BorderLayout(15, 15));
        this.battlefield = battlefield;
        this.game = game;
        this.playerGUI = playerGUI;
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setBackground(new Color(15, 23, 42)); // Slate 900

        JLabel titleLabel = new JLabel("CRÉATION DE VOTRE HÉROS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(241, 245, 249)); // Slate 100
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        mainPanel.setOpaque(false);

        // 1. Panel Classes Préfaites
        JPanel premadePanel = new JPanel(new GridLayout(3, 1, 15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 41, 59)); // Slate 800
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        premadePanel.setOpaque(false);
        premadePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Classes Préfaites",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(245, 158, 11) // Amber 500
            )
        ));
        
        JButton buttonWarrior = createStyledButton("Warrior", new Color(71, 85, 105));
        JButton buttonAssassin = createStyledButton("Assassin", new Color(71, 85, 105));
        JButton buttonMastodonte = createStyledButton("Mastodonte", new Color(71, 85, 105));
        
        buttonWarrior.addActionListener(e -> {
            this.battlefield.addHero(HeroFactory.getFactory("warrior"));
            finishSelection();
        });
        buttonAssassin.addActionListener(e -> {
            this.battlefield.addHero(HeroFactory.getFactory("assassin"));
            finishSelection();
        });
        buttonMastodonte.addActionListener(e -> {
            this.battlefield.addHero(HeroFactory.getFactory("mastodonte"));
            finishSelection();
        });

        premadePanel.add(buttonWarrior);
        premadePanel.add(buttonAssassin);
        premadePanel.add(buttonMastodonte);
        mainPanel.add(premadePanel);

        // 2. Panel Personnalisé
        JPanel customPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 41, 59)); // Slate 800
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        customPanel.setOpaque(false);
        customPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Héros Personnalisé",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(245, 158, 11) // Amber 500
            )
        ));

        JPanel statsPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        statsPanel.setOpaque(false);
        
        labelPointsLeft = new JLabel("Points restants : " + pointsLeft, SwingConstants.CENTER);
        labelPointsLeft.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelPointsLeft.setForeground(new Color(245, 158, 11));
        customPanel.add(labelPointsLeft, BorderLayout.NORTH);

        // HP row
        JPanel hpRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        hpRow.setOpaque(false);
        btnHpMinus = createSmallButton("-");
        btnHpPlus = createSmallButton("+");
        labelHp = createLabel();
        btnHpMinus.addActionListener(e -> { ptsHp--; pointsLeft++; updateCustomFields(); });
        btnHpPlus.addActionListener(e -> { ptsHp++; pointsLeft--; updateCustomFields(); });
        hpRow.add(btnHpMinus);
        hpRow.add(btnHpPlus);
        hpRow.add(labelHp);
        statsPanel.add(hpRow);

        // Ammo row
        JPanel ammoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        ammoRow.setOpaque(false);
        btnAmmoMinus = createSmallButton("-");
        btnAmmoPlus = createSmallButton("+");
        labelAmmo = createLabel();
        btnAmmoMinus.addActionListener(e -> { ptsAmmo--; pointsLeft++; updateCustomFields(); });
        btnAmmoPlus.addActionListener(e -> { ptsAmmo++; pointsLeft--; updateCustomFields(); });
        ammoRow.add(btnAmmoMinus);
        ammoRow.add(btnAmmoPlus);
        ammoRow.add(labelAmmo);
        statsPanel.add(ammoRow);

        // Shield row
        JPanel shieldRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        shieldRow.setOpaque(false);
        btnShieldMinus = createSmallButton("-");
        btnShieldPlus = createSmallButton("+");
        labelShield = createLabel();
        btnShieldMinus.addActionListener(e -> { ptsShield--; pointsLeft++; updateCustomFields(); });
        btnShieldPlus.addActionListener(e -> { ptsShield++; pointsLeft--; updateCustomFields(); });
        shieldRow.add(btnShieldMinus);
        shieldRow.add(btnShieldPlus);
        shieldRow.add(labelShield);
        statsPanel.add(shieldRow);

        // Mine row
        JPanel mineRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        mineRow.setOpaque(false);
        btnMineMinus = createSmallButton("-");
        btnMinePlus = createSmallButton("+");
        labelMine = createLabel();
        btnMineMinus.addActionListener(e -> { ptsMine--; pointsLeft++; updateCustomFields(); });
        btnMinePlus.addActionListener(e -> { ptsMine++; pointsLeft--; updateCustomFields(); });
        mineRow.add(btnMineMinus);
        mineRow.add(btnMinePlus);
        mineRow.add(labelMine);
        statsPanel.add(mineRow);

        // Bomb row
        JPanel bombRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bombRow.setOpaque(false);
        btnBombMinus = createSmallButton("-");
        btnBombPlus = createSmallButton("+");
        labelBomb = createLabel();
        btnBombMinus.addActionListener(e -> { ptsBomb--; pointsLeft++; updateCustomFields(); });
        btnBombPlus.addActionListener(e -> { ptsBomb++; pointsLeft--; updateCustomFields(); });
        bombRow.add(btnBombMinus);
        bombRow.add(btnBombPlus);
        bombRow.add(labelBomb);
        statsPanel.add(bombRow);

        customPanel.add(statsPanel, BorderLayout.CENTER);

        JButton btnCreateCustom = createStyledButton("Créer mon Héros", new Color(245, 158, 11));
        btnCreateCustom.setForeground(Color.BLACK);
        btnCreateCustom.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCreateCustom.addActionListener(e -> {
            int hp = 50 + ptsHp * 10;
            int ammo = 2 + ptsAmmo * 2;
            int shield = ptsShield;
            int mine = ptsMine;
            int bomb = ptsBomb;
            this.battlefield.addHero(new CustomHeroFactory(hp, ammo, shield, mine, bomb));
            finishSelection();
        });
        customPanel.add(btnCreateCustom, BorderLayout.SOUTH);

        mainPanel.add(customPanel);
        this.add(mainPanel, BorderLayout.CENTER);

        updateCustomFields();
        this.setVisible(true);
    }

    private JButton createStyledButton(String text, Color baseBg) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg;
                if (!isEnabled()) {
                    bg = new Color(51, 65, 85); // Slate 700
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
                    g2.setColor(new Color(100, 116, 139)); // Slate 500
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(new Color(241, 245, 249)); // Slate 100
        button.setBackground(baseBg);
        return button;
    }

    private JButton createSmallButton(String text) {
        JButton button = createStyledButton(text, new Color(71, 85, 105));
        button.setPreferredSize(new Dimension(35, 30));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return button;
    }

    private JLabel createLabel() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(200, 20));
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(241, 245, 249)); // Slate 100
        return label;
    }

    private void updateCustomFields() {
        labelPointsLeft.setText("Points restants : " + pointsLeft);
        
        labelHp.setText("Vie : " + (50 + ptsHp * 10) + " PV (" + ptsHp + " pt)");
        labelAmmo.setText("Munitions : " + (2 + ptsAmmo * 2) + " (" + ptsAmmo + " pt)");
        labelShield.setText("Bouclier : " + ptsShield + " (" + ptsShield + " pt)");
        labelMine.setText("Mines : " + ptsMine + " (" + ptsMine + " pt)");
        labelBomb.setText("Bombes : " + ptsBomb + " (" + ptsBomb + " pt)");

        btnHpMinus.setEnabled(ptsHp > 0);
        btnHpPlus.setEnabled(pointsLeft > 0);

        btnAmmoMinus.setEnabled(ptsAmmo > 0);
        btnAmmoPlus.setEnabled(pointsLeft > 0);

        btnShieldMinus.setEnabled(ptsShield > 0);
        btnShieldPlus.setEnabled(pointsLeft > 0);

        btnMineMinus.setEnabled(ptsMine > 0);
        btnMinePlus.setEnabled(pointsLeft > 0);

        btnBombMinus.setEnabled(ptsBomb > 0);
        btnBombPlus.setEnabled(pointsLeft > 0);
    }
    
    private void finishSelection() {
        this.playerGUI.heroSelected();
    }
}
