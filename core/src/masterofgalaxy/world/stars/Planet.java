package masterofgalaxy.world.stars;

public class Planet {
    public static final Planet nullPlanet;

    static {
        nullPlanet = new Planet(PlanetClass.nullKlass);
        nullPlanet.valid = false;
    }

    public PlanetClass klass;
    private boolean valid = true;

    public Planet() {
    }

    public Planet(PlanetClass klass) {
        this.klass = klass;
    }

    public float getGrowthRateMultiplier() {
        return 1.0f;
    }

    public float getProductionMultiplier() {
        return 1.0f;
    }

    public float getResearchMultiplier() {
        return 1.0f;
    }

    public float getMaxPopulation() {
        return 80.0f;
    }

    public boolean isValid() {
        return valid;
    }
}
