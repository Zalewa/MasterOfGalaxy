package masterofgalaxy.ecs.components;

public class ColonyState {
    public float population;
    public float factories;

    public void set(ColonyState other) {
        this.population = other.population;
        this.factories = other.factories;
    }
}