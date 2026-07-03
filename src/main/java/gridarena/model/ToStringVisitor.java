package gridarena.model;

import gridarena.entity.EntityVisitor;
import gridarena.entity.consumable.AmmoKit;
import gridarena.entity.consumable.MedicalKit;
import gridarena.entity.environment.Wall;
import gridarena.entity.explosive.Barrel;
import gridarena.entity.explosive.Bomb;
import gridarena.entity.explosive.Mine;
import gridarena.entity.hero.Hero;

/**
 * Visiteur générant le rendu sous forme d'émotes textuelles des entités de la grille.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class ToStringVisitor implements EntityVisitor {
    
    private String formatted = "";

    public String getFormatted() {
        return this.formatted;
    }

    @Override
    public void visit(Wall wall) {
        this.formatted = " " + wall.getEmote() + " ";
    }

    @Override
    public void visit(MedicalKit medicalKit) {
        this.formatted = " " + medicalKit.getEmote() + " ";
    }

    @Override
    public void visit(AmmoKit ammoKit) {
        this.formatted = " " + ammoKit.getEmote() + " ";
    }

    @Override
    public void visit(Mine mine) {
        this.formatted = " " + mine.getEmote() + " ";
    }

    @Override
    public void visit(Bomb bomb) {
        this.formatted = " " + bomb.getEmote() + " ";
    }

    @Override
    public void visit(Barrel barrel) {
        this.formatted = " " + barrel.getEmote() + " ";
    }

    @Override
    public void visit(Hero hero) {
        this.formatted = " " + hero.getEmote();
    }
}
