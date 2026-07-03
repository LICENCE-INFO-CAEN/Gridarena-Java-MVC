package gridarena.entity;

import gridarena.entity.consumable.AmmoKit;
import gridarena.entity.consumable.MedicalKit;
import gridarena.entity.environment.Wall;
import gridarena.entity.explosive.Barrel;
import gridarena.entity.explosive.Bomb;
import gridarena.entity.explosive.Mine;
import gridarena.entity.hero.Hero;

/**
 * Interface représentant le patron de conception Visitor classique pour les entités du jeu (sans génériques).
 *
 * @author Florian Pépin.
 * @version 2.0
 */
public interface EntityVisitor {
    void visit(Wall wall);
    void visit(MedicalKit medicalKit);
    void visit(AmmoKit ammoKit);
    void visit(Mine mine);
    void visit(Bomb bomb);
    void visit(Barrel barrel);
    void visit(Hero hero);
}
