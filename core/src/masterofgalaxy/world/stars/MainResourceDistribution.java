package masterofgalaxy.world.stars;

import com.badlogic.gdx.math.MathUtils;
import masterofgalaxy.AskForFloat;
import masterofgalaxy.world.ui.ResourceDistributionSlider;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainResourceDistribution {
    public enum ResourceId {
        Shipyard("$shipRes"), Defense("$defenseRes"), Industry("$industryRes"), Ecology("$ecologyRes"), Research("$researchRes");

        private String localization;

        ResourceId(String localization) {
            this.localization = localization;
        }

        public String getLocalization() {
            return localization;
        }
    }

    private final static float pool = 1.0f;

    private Map<ResourceId, Resource> distribution = new LinkedHashMap<ResourceId, Resource>();

    public MainResourceDistribution() {
        for (ResourceId id : ResourceId.values()) {
            distribution.put(id, new Resource());
        }
    }

    public void set(MainResourceDistribution other) {
        for (ResourceId id : ResourceId.values()) {
            distribution.get(id).set(other.distribution.get(id));
        }
    }

    public float getDefense() {
        return getAmount(ResourceId.Defense);
    }

    public void setDefense(float defense) {
        setAmount(ResourceId.Defense, defense);
    }

    public float getEcology() {
        return getAmount(ResourceId.Ecology);
    }

    public void setEcology(float ecology) {
        setAmount(ResourceId.Ecology, ecology);
    }

    public float getIndustry() {
        return getAmount(ResourceId.Industry);
    }

    public void setIndustry(float industry) {
        setAmount(ResourceId.Industry, industry);
    }

    public float getResearch() {
        return getAmount(ResourceId.Research);
    }

    public void setResearch(float research) {
        setAmount(ResourceId.Research, research);
    }

    public float getShipyard() {
        return getAmount(ResourceId.Shipyard);
    }

    public void setShipyard(float shipyard) {
        setAmount(ResourceId.Shipyard, shipyard);
    }

    public void setLocked(ResourceId id, boolean locked) {
        distribution.get(id).locked = locked;
    }

    public boolean isLocked(ResourceId id) {
        return distribution.get(id).locked;
    }

    public void setMinAmountAsker(ResourceId resourceId, AskForFloat asker) {
        distribution.get(resourceId).minAmountAsker = asker;
    }

    public float getAmount(ResourceId id) {
        return distribution.get(id).amount;
    }

    public void setAmount(ResourceId id, float amount) {
        Resource resource = distribution.get(id);
        float oldValue = resource.amount;
        resource.setAmount(amount);
        float newValue = resource.amount;
        normalizeDistribution(id, pool - getSum());
    }

    private void normalizeDistribution(ResourceId id, float delta) {
        int idx = indexOfResourceId(id);
        for (int i = 1; i <= ResourceId.values().length; ++i) {
            ResourceId nextId = ResourceId.values()[(idx + i) % ResourceId.values().length];
            Resource resource = distribution.get(nextId);
            if (resource.locked && nextId != id) {
                continue;
            }
            float oldValue = resource.amount;
            resource.setAmount(oldValue + delta);
            float newValue = resource.amount;
            delta -= newValue - oldValue;
            if (Float.compare(getSum(), pool) == 0) {
                return;
            }
        }
    }

    private int indexOfResourceId(ResourceId id) {
        for (int i = 0; i < ResourceId.values().length; ++i) {
            if (ResourceId.values()[i] == id) {
                return i;
            }
        }
        return -1;
    }

    private float getSum() {
        float sum = 0.0f;
        for (Resource resource : distribution.values()) {
            sum += resource.amount;
        }
        return sum;
    }

    private class Resource {
        private float amount;

        public boolean locked;
        public AskForFloat minAmountAsker;

        public void set(Resource other) {
            locked = other.locked;
            amount = other.amount;
            minAmountAsker = other.minAmountAsker;
        }

        public void setAmount(float value) {
            float minAmount = 0.0f;
            if (minAmountAsker != null) {
                minAmount = minAmountAsker.ask();
            }
            amount = MathUtils.clamp(value, minAmount, pool);
        }
    }
}
