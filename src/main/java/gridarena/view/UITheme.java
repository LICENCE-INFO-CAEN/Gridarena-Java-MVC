package gridarena.view;

import java.awt.Color;
import java.awt.Font;

/**
 * Palette de couleurs et polices du thème "Tactical HQ".
 * Point central unique de définition : modifier ici change tout l'UI.
 *
 * Palette :
 *   Fond principal  → charcoal chaud #1C1C1E  (pas de bleu-gris)
 *   Fond panneau    → #252528
 *   Fond champ      → #2D2D30
 *   Accent          → crimson  #C0392B
 *   Accent survol   → #E74C3C
 *   Texte principal → #F0F0F0
 *   Texte secondaire→ #A0A0A0
 *   Texte muet      → #606060
 *   Bordure         → #3A3A3C
 *   Succès          → #27AE60
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public final class UITheme {

    private UITheme() {}

    // ── Fonds ─────────────────────────────────────────────────────────────────
    public static final Color BG_PRIMARY   = new Color(0x1C, 0x1C, 0x1E);
    public static final Color BG_SECONDARY = new Color(0x25, 0x25, 0x28);
    public static final Color BG_TERTIARY  = new Color(0x2D, 0x2D, 0x30);

    // ── Accent ────────────────────────────────────────────────────────────────
    public static final Color ACCENT       = new Color(0xC0, 0x39, 0x2B);
    public static final Color ACCENT_HOVER = new Color(0xE7, 0x4C, 0x3C);

    // ── Texte ─────────────────────────────────────────────────────────────────
    public static final Color TEXT_PRIMARY   = new Color(0xF0, 0xF0, 0xF0);
    public static final Color TEXT_SECONDARY = new Color(0xA0, 0xA0, 0xA0);
    public static final Color TEXT_MUTED     = new Color(0x60, 0x60, 0x60);

    // ── Structure ─────────────────────────────────────────────────────────────
    public static final Color BORDER  = new Color(0x3A, 0x3A, 0x3C);
    public static final Color SUCCESS = new Color(0x27, 0xAE, 0x60);

    // ── Typographie ───────────────────────────────────────────────────────────
    public static final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD,  18);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONT_LABEL    = new Font("Segoe UI", Font.BOLD,  11);
    public static final Font FONT_BODY     = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BADGE    = new Font("Segoe UI", Font.BOLD,  10);
}
