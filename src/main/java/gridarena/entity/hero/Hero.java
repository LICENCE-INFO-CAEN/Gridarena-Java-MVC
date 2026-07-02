package gridarena.entity.hero;

import gridarena.entity.Entity;

/**
 * Représente le Hero que le joueur incarne.
 * 
 * @author Florian Pépin.
 * @version 1.0
 */
public abstract class Hero extends Entity {
    
    public static final int WEAPON_DAMAGE = 5;
    public static final int AX_DAMAGE = 3;
    private String specialization;
    private int healthMax;
    private int healthRemaining;
    private int ammoMax;
    private int ammoRemaining;
    private int shieldMax;
    private int shieldRemaining;
    private int mineMax;
    private int mineRemaining;
    private int bombMax;
    private int bombRemaining;
    private boolean immune;

    protected Hero(String specialization, int healthMax, int ammoMax, int shieldMax, int mineMax, int bombMax, int x, int y, String image) {
        super(x, y, "🪖​", image, false);
        this.specialization = specialization;
        this.healthMax = healthMax;
        this.healthRemaining = healthMax;
        this.ammoMax = ammoMax;
        this.ammoRemaining = ammoMax;
        this.shieldMax = shieldMax;
        this.shieldRemaining = shieldMax;
        this.mineMax = mineMax;
        this.mineRemaining = mineMax;
        this.bombMax = bombMax;
        this.bombRemaining = bombMax;
        this.immune = false;
    }

    /**
     * Récupérer la spécialisation du joueur.
     * 
     * @return la spécialisation du joueur.
     */
    public String getSpecialization() {
        return this.specialization;
    }

    /**
     * Récupérer les points de vie maximum du joueur.
     * 
     * @return les points de vie maximum du joueur.
     */
    public int getHealthMax() {
        return this.healthMax;
    }

    /**
     * Récupérer la quantité de points de vie restant du joueur.
     * 
     * @return la quantité de points de vie restant du joueur.
     */
    public int getHealthRemaining() {
        return this.healthRemaining;
    }

    /**
     * Récupérer la quantité de munitions maximum du joueur.
     * 
     * @return la quantité de munitions maximum du joueur.
     */
    public int getAmmoMax() {
        return ammoMax;
    }

    /**
     * Récupérer la quantité de munitions restant du joueur.
     * 
     * @return la quantité de munitions restant du joueur.
     */
    public int getAmmoRemaining() {
        return ammoRemaining;
    }

    /**
     * Récupérer la quantité de boucliers maximum du joueur.
     * 
     * @return la quantité de boucliers maximum du joueur.
     */
    public int getShieldMax() {
        return this.shieldMax;
    }

    /**
     * Récupérer la quantité de boucliers restant du joueur.
     * 
     * @return la quantité de boucliers restant du joueur.
     */
    public int getShieldRemaining() {
        return this.shieldRemaining;
    }

    /**
     * Récupérer la quantité de mines maximum du joueur.
     * 
     * @return la quantité de mines maximum du joueur.
     */
    public int getMineMax() {
        return this.mineMax;
    }

    /**
     * Récupérer la quantité de mines restant du joueur.
     * 
     * @return la quantité de mines restant du joueur.
     */
    public int getMineRemaining() {
        return this.mineRemaining;
    }

    /**
     * Récupérer la quantité de bombs maximum du joueur.
     * 
     * @return la quantité de bombs maximum du joueur.
     */
    public int getBombMax() {
        return this.bombMax;
    }

    /**
     * Récupérer la quantité de bombs restant du joueur.
     * 
     * @return la quantité de bombs restant du joueur.
     */
    public int getBombRemaining() {
        return this.bombRemaining;
    }

    /**
     * Récupérer l'état d'immunité du joueur.
     * 
     * @return l'état d'immunité du joueur.
     */
    public boolean isImmune() {
        return this.immune;
    }

    /**
     * Modifier la quantité de points de vie restant du joueur.
     * 
     * @param healthRemaining la nouvelle quantité de points de vie restant du joueur.
     */
    public void setHealthRemaining(int healthRemaining) {
        if (healthRemaining < 0) {
            this.healthRemaining = 0;
        } else {
            this.healthRemaining = healthRemaining;   
        }
        this.fireChange();
    }

    /**
     * Modifier la quantité de munitions restant du joueur.
     * 
     * @param ammoRemaining la nouvelle quantité de munitions restant du joueur.
     */
    public void setAmmoRemaining(int ammoRemaining) {
        if (ammoRemaining < 0) {
            this.ammoRemaining = 0;
        } else {
            this.ammoRemaining = ammoRemaining;   
        }
        this.fireChange();
    }

    /**
     * Modifier la quantité de boucliers restant du joueur.
     * 
     * @param shieldRemaining la nouvelle quantité de boucliers restant du joueur.
     */
    public void setShieldRemaining(int shieldRemaining) {
        if (shieldRemaining < 0) {
            this.shieldRemaining = 0;
        } else {
            this.shieldRemaining = shieldRemaining;   
        }
        this.fireChange();
    }
    /**
     * Modifier la quantité de mines restant du joueur.
     * 
     * @param mineRemaining la nouvelle quantité de mines restant du joueur.
     */
    public void setMineRemaining(int mineRemaining) {
        if (mineRemaining < 0) {
            this.mineRemaining = 0;
        } else {
            this.mineRemaining = mineRemaining;   
        }
        this.fireChange();
    }
    /**
     * Modifier la quantité de bombs restant du joueur.
     * 
     * @param bombRemaining la nouvelle quantité de bombs restant du joueur.
     */
    public void setBombRemaining(int bombRemaining) {
        if (bombRemaining < 0) {
            this.bombRemaining = 0;
        } else {
            this.bombRemaining = bombRemaining;   
        }
        this.fireChange();
    }

    /**
     * Modifier l'état d'immunité du joueur.
     * 
     * @param state le nouvel état d'immunité du joueur.
     */
    public void setImmune(boolean state) {
        this.immune = state;
        this.fireChange();
    }

    public boolean isAlive() {
        return this.healthRemaining > 0;
    }
    
    public boolean hasShieldsLeft() {
        return this.shieldRemaining > 0;
    }
    
    public boolean hasMinesLeft() {
        return this.mineRemaining > 0;
    }
    
    public boolean hasBombsLeft() {
        return this.bombRemaining > 0;
    }
    
    /**
     * Utiliser une mine en décrémentant le stock si disponible.
     * 
     * @return true si une mine a été consommée, false sinon.
     */
    public boolean useMine() {
        if (this.mineRemaining > 0) {
            this.mineRemaining--;
            this.fireChange();
            return true;
        }
        return false;
    }

    /**
     * Utiliser une bombe en décrémentant le stock si disponible.
     * 
     * @return true si une bombe a été consommée, false sinon.
     */
    public boolean useBomb() {
        if (this.bombRemaining > 0) {
            this.bombRemaining--;
            this.fireChange();
            return true;
        }
        return false;
    }
    
    /**
     * Tirer sur un joueur.
     * 
     * @param h hero cible.
     */
    public void shootHero(Hero h) {
        h.setHealthRemaining(h.getHealthRemaining()-Hero.WEAPON_DAMAGE);
    }
    
    /**
     * Attaquer un joueur avec la hache.
     * 
     * @param h hero cible.
     */
    public void axAttack(Hero h) {
        h.setHealthRemaining(h.getHealthRemaining()-Hero.AX_DAMAGE);
    }
       


    @Override
    public String toString() {
        return super.toString()+"\nhealth = "+healthRemaining+"/"+healthMax+"\nshields = "
                +shieldRemaining+"/"+shieldMax+"\nmines = "
                +mineRemaining+ "/" +mineMax+"\nbombMax = "
                +bombRemaining+"/"+bombMax;
    }

}
