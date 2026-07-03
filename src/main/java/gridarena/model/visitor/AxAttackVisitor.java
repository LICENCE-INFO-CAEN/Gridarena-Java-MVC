package gridarena.model.visitor;

import gridarena.entity.EntityVisitor;
import gridarena.entity.consumable.AmmoKit;
import gridarena.entity.consumable.MedicalKit;
import gridarena.entity.environment.Wall;
import gridarena.entity.explosive.Barrel;
import gridarena.entity.explosive.Bomb;
import gridarena.entity.explosive.Mine;
import gridarena.entity.hero.Hero;
import gridarena.model.Battlefield;

/**
 * Visiteur gérant l'application d'une attaque à la hache sur la cible.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class AxAttackVisitor implements EntityVisitor {
    
    private final Hero attacker;
    private final Battlefield battlefield;
    private boolean hit = false;

    public AxAttackVisitor(Hero attacker, Battlefield battlefield) {
        this.attacker = attacker;
        this.battlefield = battlefield;
    }

    public boolean hasHit() {
        return this.hit;
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
    public void visit(Hero victim) {
        if (victim.isImmune()) {
            this.hit = true;
            return;
        }
        attacker.axAttack(victim);
        battlefield.isHeroDead(victim);
        battlefield.notifyChange();
        this.hit = true;
    }
}
