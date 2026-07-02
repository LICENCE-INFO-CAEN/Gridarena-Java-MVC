package gridarena.model;

/**
 * Conteneur de données (DTO) regroupant les paramètres de configuration d'une partie.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class GameConfig {
    
    private final int gridSize;
    private final int walls;
    private final int medKits;
    private final int ammoKits;
    private final int barrels;
    private final int guiPlayers;
    private final int cliPlayers;
    private final int botPlayers;
    private final String botStrategy;
    private final String fillStrategy;

    public GameConfig(int gridSize, int walls, int medKits, int ammoKits, int barrels, 
                      int guiPlayers, int cliPlayers, int botPlayers, 
                      String botStrategy, String fillStrategy) {
        this.gridSize = gridSize;
        this.walls = walls;
        this.medKits = medKits;
        this.ammoKits = ammoKits;
        this.barrels = barrels;
        this.guiPlayers = guiPlayers;
        this.cliPlayers = cliPlayers;
        this.botPlayers = botPlayers;
        this.botStrategy = botStrategy;
        this.fillStrategy = fillStrategy;
    }

    public int getGridSize() {
        return gridSize;
    }

    public int getWalls() {
        return walls;
    }

    public int getMedKits() {
        return medKits;
    }

    public int getAmmoKits() {
        return ammoKits;
    }

    public int getBarrels() {
        return barrels;
    }

    public int getGuiPlayers() {
        return guiPlayers;
    }

    public int getCliPlayers() {
        return cliPlayers;
    }

    public int getBotPlayers() {
        return botPlayers;
    }

    public String getBotStrategy() {
        return botStrategy;
    }

    public String getFillStrategy() {
        return fillStrategy;
    }
    
}
