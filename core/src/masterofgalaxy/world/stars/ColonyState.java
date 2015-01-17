package masterofgalaxy.world.stars;

public class ColonyState {
    public float population;
    public float factories;
    public float defenseBases;
    public MainResourceDistribution mainResourceDistribution = new MainResourceDistribution();

    public void set(ColonyState other) {
        this.population = other.population;
        this.factories = other.factories;
        this.defenseBases = other.defenseBases;
        this.mainResourceDistribution.setValues(other.mainResourceDistribution);
    }

    public void reset() {
        population = 0.0f;
        factories = 0.0f;
        defenseBases = 0.0f;
        mainResourceDistribution.setIndustry(1.0f);
    }
}
