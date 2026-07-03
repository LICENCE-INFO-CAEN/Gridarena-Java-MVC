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
 * Visiteur gérant l'interaction lors du déplacement d'un héros sur une entité.
 *
 * @author Florian Pépin.
 * @version 1.0
 */
public class MoveVisitor implements EntityVisitor {
    
    private final Hero hero;
    private final int x;
    private final int y;
    private final Battlefield battlefield;
    private boolean moved = false;

    public MoveVisitor(Hero hero, int x, int y, Battlefield battlefield) {
        this.hero = hero;
        this.x = x;
        this.y = y;
        this.battlefield = battlefield;
    }

    public boolean hasMoved() {
        return this.moved;
    }

    @Override
    public void visit(Wall wall) {
        // Un mur bloque le déplacement.
    }

    @Override
    public void visit(MedicalKit medicalKit) {
        medicalKit.useConsumable(hero);
        battlefield.moveHeroToNewPosition(hero, x, y);
        this.moved = true;
    }

    @Override
    public void visit(AmmoKit ammoKit) {
        ammoKit.useConsumable(hero);
        battlefield.moveHeroToNewPosition(hero, x, y);
        this.moved = true;
    }

    @Override
    public void visit(Mine mine) {
        if (mine.isWalkable()) {
            battlefield.detonate(mine);
            battlefield.moveHeroToNewPosition(hero, x, y);
            battlefield.isHeroDead(hero);
            this.moved = true;
        }
    }

    @Override
    public void visit(Bomb bomb) {
        if (bomb.isWalkable()) {
            battlefield.detonate(bomb);
            battlefield.moveHeroToNewPosition(hero, x, y);
            battlefield.isHeroDead(hero);
            this.moved = true;
        }
    }

    @Override
    public void visit(Barrel barrel) {
        if (barrel.isWalkable()) {
            battlefield.detonate(barrel);
            battlefield.moveHeroToNewPosition(hero, x, y);
            battlefield.isHeroDead(hero);
            this.moved = true;
        }
    }

    @Override
    public void visit(Hero targetHero) {
        // Impossible de marcher sur un autre héros.
    }
}
