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
 * Visiteur gérant les collisions et effets d'un projectile de pistolet sur la grille.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class ShootHeroVisitor implements EntityVisitor {
    
    private final Hero shooter;
    private final Battlefield battlefield;
    private Boolean hitObstacle = null;

    public ShootHeroVisitor(Hero shooter, Battlefield battlefield) {
        this.shooter = shooter;
        this.battlefield = battlefield;
    }

    public Boolean getHitObstacle() {
        return this.hitObstacle;
    }

    @Override
    public void visit(Wall wall) {
        this.hitObstacle = false; // Bloque et arrête le tir.
    }

    @Override
    public void visit(MedicalKit medicalKit) {
        // Le tir traverse.
    }

    @Override
    public void visit(AmmoKit ammoKit) {
        // Le tir traverse.
    }

    @Override
    public void visit(Mine mine) {
        // Le tir traverse.
    }

    @Override
    public void visit(Bomb bomb) {
        // Le tir traverse.
    }

    @Override
    public void visit(Barrel barrel) {
        battlefield.detonate(barrel);
        this.hitObstacle = true; // Fait exploser et arrête le tir.
    }

    @Override
    public void visit(Hero victim) {
        if (victim.isImmune()) {
            this.hitObstacle = true;
            return;
        }
        shooter.shootHero(victim);
        battlefield.isHeroDead(victim);
        battlefield.notifyChange();
        this.hitObstacle = true; // Touche et arrête le tir.
    }
}
