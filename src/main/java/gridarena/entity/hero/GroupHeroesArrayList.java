package gridarena.entity.hero;

import gridarena.utils.*;
import java.util.*;

/**
 * Représente un groupe de héro dans une ArrayList.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class GroupHeroesArrayList extends AbstractListenableModel implements GroupHeroesEditable, ModelListener {

    private final ArrayList<Hero> heroes;
    
    public GroupHeroesArrayList() {
        this.heroes = new ArrayList<>();
    }
    
    /**
     * Ajouter un héro au groupe.
     * 
     * @param h le héro à ajouter.
     */
    public void addHero(Hero h) {
        h.addModelListener(this);
        this.heroes.add(h);
        this.fireChange();
    }
    
    @Override
    public int getSize() {
        return this.heroes.size();
    }
    
    @Override
    public Hero getHero(int index) {
        return this.heroes.get(index);
    }
    
    @Override
    public void updatedModel(Object source) {
        this.fireChange();
    }
    
}
