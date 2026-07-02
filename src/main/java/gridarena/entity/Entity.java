package gridarena.entity;

import gridarena.utils.AbstractListenableModel;

/**
 * Représente une entité dans la grille du jeu.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public abstract class Entity extends AbstractListenableModel {
    
    protected int x;
    protected int y;
    protected final String emote;
    protected final boolean walkable;
    protected final String image;
    private static final String PATH_IMAGE = "/resources/images/";

    public Entity(int x, int y, String emote, String image, boolean walkable) {
        this.x = x;
        this.y = y;
        this.emote = emote;
        this.walkable = walkable;
        this.image = image;
    }

    public String getImageUrl() {
        return PATH_IMAGE + image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getEmote() {
        return emote;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setX(int x) {
        this.x = x;
        this.fireChange();
    }

    public void setY(int y) {
        this.y = y;
        this.fireChange();
    }

    @Override
    public String toString() {
        return "Entité ("+this.x+", "+this.y+") :\n";
    }
    
}
