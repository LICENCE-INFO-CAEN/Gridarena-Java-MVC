package gridarena.utils;

/**
 * Représente un modèle écoutable.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public interface ListenableModel {
    
    void addModelListener(ModelListener m);
    
    void removeModelListener(ModelListener m);
    
}
