package gridarena.controller;

import gridarena.model.BattlefieldModel;
import gridarena.view.gui.PlayerGUI;
import gridarena.entity.hero.HeroFactory;
import gridarena.entity.hero.CustomHeroFactory;
import gridarena.view.UITheme;

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
        this.setBackground(UITheme.BG_PRIMARY);

        JLabel titleLabel = new JLabel("CRÉATION DE VOTRE HÉROS", SwingConstants.CENTER);
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        mainPanel.setOpaque(false);

        // 1. Panel Classes Préfaites
        JPanel premadePanel = new JPanel(new GridLayout(3, 1, 12, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(UITheme.BG_SECONDARY);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Bordure gauche crimson
                g2.setColor(UITheme.ACCENT);
                g2.fillRect(0, 0, 3, getHeight());
                g2.dispose();
            }
        };
        premadePanel.setOpaque(false);
        premadePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 10, 5, 5),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "CLASSES PRÉFAITES",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                UITheme.FONT_BADGE,
                UITheme.TEXT_SECONDARY
            )
        ));
        
        JButton buttonWarrior    = createHeroCardButton("Warrior");
        JButton buttonAssassin   = createHeroCardButton("Assassin");
        JButton buttonMastodonte = createHeroCardButton("Mastodonte");
        
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
                g2.setColor(UITheme.BG_SECONDARY);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Bordure gauche crimson
                g2.setColor(UITheme.ACCENT);
                g2.fillRect(0, 0, 3, getHeight());
                g2.dispose();
            }
        };
        customPanel.setOpaque(false);
        customPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 10, 5, 5),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "HÉROS PERSONNALISÉ",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                UITheme.FONT_BADGE,
                UITheme.TEXT_SECONDARY
            )
        ));

        JPanel statsPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        statsPanel.setOpaque(false);
        
        labelPointsLeft = new JLabel("Points restants : " + pointsLeft, SwingConstants.CENTER);
        labelPointsLeft.setFont(UITheme.FONT_SUBTITLE);
        labelPointsLeft.setForeground(UITheme.ACCENT);
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

        JButton btnCreateCustom = createStyledButton("CRÉER MON HÉROS", UITheme.ACCENT);
        btnCreateCustom.setForeground(UITheme.TEXT_PRIMARY);
        btnCreateCustom.setFont(UITheme.FONT_SUBTITLE);
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

    /** Carte classe hero : fond BG_TERTIARY, bordure gauche crimson au survol. */
    private JButton createHeroCardButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color bg;
                if      (!isEnabled())           bg = new Color(0x22, 0x22, 0x24);
                else if (getModel().isPressed())  bg = UITheme.ACCENT.darker();
                else if (getModel().isRollover()) bg = UITheme.BG_SECONDARY;
                else                              bg = UITheme.BG_TERTIARY;
                g2.setColor(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Barre gauche crimson
                g2.setColor(UITheme.ACCENT);
                g2.fillRect(0, 0, 4, getHeight());
                g2.setColor(isEnabled() ? UITheme.TEXT_PRIMARY : UITheme.TEXT_MUTED);
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
        button.setFont(UITheme.FONT_SUBTITLE);
        button.setForeground(UITheme.TEXT_PRIMARY);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createStyledButton(String text, Color baseBg) {
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
        button.setFont(UITheme.FONT_SUBTITLE);
        button.setForeground(UITheme.TEXT_PRIMARY);
        button.setBackground(baseBg);
        return button;
    }

    private JButton createSmallButton(String text) {
        JButton button = createStyledButton(text, UITheme.BG_TERTIARY);
        button.setPreferredSize(new Dimension(30, 26));
        button.setFont(UITheme.FONT_SUBTITLE);
        return button;
    }

    private JLabel createLabel() {
        JLabel label = new JLabel();
        label.setFont(UITheme.FONT_BODY);
        label.setForeground(UITheme.TEXT_SECONDARY);
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
