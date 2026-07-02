package gridarena;

import gridarena.controller.GameController;
import gridarena.controller.bot.*;
import gridarena.model.fillgrid.*;

import java.io.*;
import java.util.*;

/**
 * Représente la classe principale qui lance le jeu.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class Main {
    
    private static boolean SHOW_OVERVIEW;
    private static int GUI_PLAYERS, CLI_PLAYERS, BOT_PLAYERS, WALLS, MEDICAL_KITS, AMMO_KITS, BARRELS, SIZE_GRID;
    //Stratégies : new PatternFillStrategy() ou new RandomFillStrategy() ou new ModuloFillStrategy()
    private static FillStrategy FILL_STRATEGY = new PatternFillStrategy();
    //Stratégies : AStarBotStrategy() ou DijkstraBotStrategy() ou GreedyBotStrategy()
    private static BotStrategy BOT_STRATEGY;
    
    static {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("config.properties"));
            SHOW_OVERVIEW = Boolean.parseBoolean(prop.getProperty("SHOW_OVERVIEW", "false"));
            GUI_PLAYERS = Integer.parseInt(prop.getProperty("GUI_PLAYERS"));
            CLI_PLAYERS = Integer.parseInt(prop.getProperty("CLI_PLAYERS"));
            BOT_PLAYERS = Integer.parseInt(prop.getProperty("BOT_PLAYERS"));
            SIZE_GRID = Integer.parseInt(prop.getProperty("SIZE_GRID"));
            WALLS = Integer.parseInt(prop.getProperty("WALLS"));
            MEDICAL_KITS = Integer.parseInt(prop.getProperty("MEDICAL_KITS"));
            AMMO_KITS = Integer.parseInt(prop.getProperty("AMMO_KITS"));
            BARRELS = Integer.parseInt(prop.getProperty("BARRELS"));
            String strategyProp = prop.getProperty("BOT_STRATEGY", "astar");
            switch (strategyProp.toLowerCase()) {
                case "dijkstra":
                    BOT_STRATEGY = new DijkstraBotStrategy();
                    break;
                case "greedy":
                    BOT_STRATEGY = new GreedyBotStrategy();
                    break;
                case "astar":
                default:
                    BOT_STRATEGY = new AStarBotStrategy();
                    break;
            }
        } catch (IOException ex) {
            SHOW_OVERVIEW = true;
            GUI_PLAYERS = 2;
            CLI_PLAYERS = 0;
            BOT_PLAYERS = 1;
            SIZE_GRID = 7;
            WALLS = 3;
            MEDICAL_KITS = 4;
            AMMO_KITS = 4;
            BARRELS = 3;
            BOT_STRATEGY = new AStarBotStrategy();
        }
    }
    
    /**
     * Lancer le jeu.
     * 
     * @param args les arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        if ((GUI_PLAYERS+CLI_PLAYERS+BOT_PLAYERS)*2 > SIZE_GRID || WALLS > SIZE_GRID || MEDICAL_KITS > SIZE_GRID || AMMO_KITS > SIZE_GRID || BARRELS > SIZE_GRID) {
            throw new Error("Erreur : veuillez respecter les règles isncritent dans le dossier config.properties ! ");
        }
        GameController game = new GameController(SHOW_OVERVIEW, GUI_PLAYERS, CLI_PLAYERS, BOT_PLAYERS, SIZE_GRID, WALLS, MEDICAL_KITS, AMMO_KITS, BARRELS, FILL_STRATEGY);
        game.demarrer(BOT_STRATEGY);
    }
    
}
