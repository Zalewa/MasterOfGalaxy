package masterofgalaxy;

public class ChanceRange<T> {
    public T item;
    public float minChance;
    public float maxChance;

    public ChanceRange(T item, float minChance, float maxChance) {
        this.item = item;
        this.maxChance = maxChance;
        this.minChance = minChance;
    }
}
