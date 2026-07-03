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
 * Visiteur gérant la détection et détonation d'un explosif existant lorsqu'on essaie d'en poser un nouveau.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class AddExplosiveVisitor implements EntityVisitor {
    
    private final Battlefield battlefield;
    private boolean explosive = false;

    public AddExplosiveVisitor(Battlefield battlefield) {
        this.battlefield = battlefield;
    }

    public boolean isExplosive() {
        return this.explosive;
    }

    @Override
    public void visit(Wall wall) {}

    @Override
    public void visit(MedicalKit medicalKit) {}

    @Override
    public void visit(AmmoKit ammoKit) {}

    @Override
    public void visit(Mine mine) {
        battlefield.detonate(mine);
        this.explosive = true;
    }

    @Override
    public void visit(Bomb bomb) {
        battlefield.detonate(bomb);
        this.explosive = true;
    }

    @Override
    public void visit(Barrel barrel) {
        battlefield.detonate(barrel);
        this.explosive = true;
    }

    @Override
    public void visit(Hero hero) {}
}
