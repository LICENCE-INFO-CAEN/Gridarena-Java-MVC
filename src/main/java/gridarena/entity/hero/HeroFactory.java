package gridarena.entity.hero;

/**
 * Créateur abstrait pour le Design Pattern Factory Method servant à instancier les Hero.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public abstract class HeroFactory {

    /**
     * Méthode de fabrique (Factory Method) pour créer un Hero.
     * 
     * @param x position horizontale
     * @param y position verticale
     * @return une instance de Hero
     */
    public abstract Hero createHero(int x, int y);

    /**
     * Retourne la fabrique appropriée selon la spécialisation demandée.
     * 
     * @param specialization la spécialisation souhaitée ("assassin", "warrior", "mastodonte", etc.)
     * @return une instance de HeroFactory correspondante
     */
    public static HeroFactory getFactory(String specialization) {
        if (specialization == null) {
            return new DefaultHeroFactory();
        }
        switch (specialization.toLowerCase()) {
            case "assassin":
                return new AssassinFactory();
            case "mastodonte":
                return new MastodonteFactory();
            case "warrior":
                return new WarriorFactory();
            default:
                return new DefaultHeroFactory();
        }
    }
}
