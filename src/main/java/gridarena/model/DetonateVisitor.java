package gridarena.model;

import gridarena.entity.EntityVisitor;
import gridarena.entity.consumable.AmmoKit;
import gridarena.entity.consumable.MedicalKit;
import gridarena.entity.environment.Wall;
import gridarena.entity.explosive.Barrel;
import gridarena.entity.explosive.Bomb;
import gridarena.entity.explosive.Explosive;
import gridarena.entity.explosive.Mine;
import gridarena.entity.hero.Hero;

/**
 * Visiteur gérant l'application des dégâts d'une explosion sur les entités proches.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class DetonateVisitor implements EntityVisitor {
    
    private final Explosive explosive;
    private final Battlefield battlefield;

    public DetonateVisitor(Explosive explosive, Battlefield battlefield) {
        this.explosive = explosive;
        this.battlefield = battlefield;
    }

    @Override
    public void visit(Wall wall) {}

    @Override
    public void visit(MedicalKit medicalKit) {}

    @Override
    public void visit(AmmoKit ammoKit) {}

    @Override
    public void visit(Mine mine) {}

    @Override
    public void visit(Bomb bomb) {}

    @Override
    public void visit(Barrel barrel) {}

    @Override
    public void visit(Hero hero) {
        explosive.explode(hero);
        battlefield.isHeroDead(hero);
    }
}
