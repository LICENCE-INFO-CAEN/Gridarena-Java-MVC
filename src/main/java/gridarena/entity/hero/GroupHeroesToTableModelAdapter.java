package gridarena.entity.hero;

import gridarena.utils.ModelListener;
import javax.swing.table.AbstractTableModel;

/**
 * Représente un adapteur d'un groupe de héros vers un JTable.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public class GroupHeroesToTableModelAdapter extends AbstractTableModel implements ModelListener {
    
    private final GroupHeroesArrayList group;
    private String[] columns = {"Spécialité", "Vie", "Munitions", "Mines", "Bombes", "Shields", "position"};

    public GroupHeroesToTableModelAdapter(GroupHeroesArrayList group) {
        this.group = group;
        this.group.addModelListener(this);
    }

    @Override
    public int getRowCount() {
        return this.group.getSize();
    }

    @Override
    public int getColumnCount() {
        return this.columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Hero hero = group.getHero(rowIndex);
        switch (columnIndex) {
            case 0: return hero.getSpecialization();
            case 1: return hero.isAlive() ? hero.getHealthRemaining()+"/"+hero.getHealthMax() : "mort";  
            case 2: return hero.getAmmoRemaining()+"/"+hero.getAmmoMax();
            case 3: return hero.getMineRemaining()+"/"+hero.getMineMax();
            case 4: return hero.getBombRemaining()+"/"+hero.getBombMax();
            case 5: return hero.getShieldRemaining()+"/"+hero.getShieldMax();
            case 6: return "("+hero.getX()+","+hero.getY()+")";
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return this.columns[column];
    }
    
    @Override
    public void updatedModel(Object source) {
        this.fireTableDataChanged();
    }
    
}
