package gridarena.utils;

import java.util.*;

/**
 * Représente un modèle écoutable.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public abstract class AbstractListenableModel implements ListenableModel {

    protected List<ModelListener> observers;
    
    public AbstractListenableModel() {
        this.observers = new ArrayList<>();
    }
    
    @Override
    public void addModelListener(ModelListener m) {
        this.observers.add(m);
    }
    
    @Override
    public void removeModelListener(ModelListener m) {
        this.observers.remove(m);
    }

    protected void fireChange() {
        for (ModelListener m : this.observers) {
            m.updatedModel(this);
        }
    }

}