package masterofgalaxy.world.stars;

public class ColonyState {
    public float population;
    public float factories;
    public MainResourceDistribution mainResourceDistribution = new MainResourceDistribution();

    public void set(ColonyState other) {
        this.population = other.population;
        this.factories = other.factories;
        this.mainResourceDistribution.set(mainResourceDistribution);
    }
}