package masterofgalaxy.world.stars;

import masterofgalaxy.AskForFloat;

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

    public final static float pool = 1.0f;

    private Map<String, Resource> distribution = new LinkedHashMap<String, Resource>();

    public MainResourceDistribution() {
        for (ResourceId id : ResourceId.values()) {
            distribution.put(id.name(), new Resource());
        }
    }

    public void setValues(MainResourceDistribution other) {
        for (ResourceId id : ResourceId.values()) {
            distribution.get(id.name()).setValues(other.distribution.get(id.name()));
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
        distribution.get(id.name()).locked = locked;
    }

    public boolean isLocked(ResourceId id) {
        return distribution.get(id.name()).locked;
    }

    public void setMinAmountAsker(ResourceId resourceId, AskForFloat asker) {
        distribution.get(resourceId.name()).minAmountAsker = asker;
    }

    public float getAmount(ResourceId id) {
        return distribution.get(id.name()).getAmount();
    }

    public void setAmount(ResourceId id, float amount) {
        Resource resource = distribution.get(id.name());
        float oldValue = resource.getAmount();
        resource.setAmount(amount);
        float newValue = resource.getAmount();
        normalizeDistribution(id, pool - getSum());
    }

    public void clearAndDistributeElsewhere(ResourceId clearedId, ResourceId targetId) {
        Resource source = distribution.get(clearedId.name());
        Resource target = distribution.get(targetId.name());
        float amount = source.getAmount();
        source.setAmount(0.0f);
        float delta = amount - source.getAmount();
        target.setAmount(target.getAmount() + delta);
    }

    private void normalizeDistribution(ResourceId id, float delta) {
        int idx = indexOfResourceId(id);
        for (int i = 1; i <= ResourceId.values().length; ++i) {
            ResourceId nextId = ResourceId.values()[(idx + i) % ResourceId.values().length];
            Resource resource = distribution.get(nextId.name());
            if (resource.locked && nextId != id) {
                continue;
            }
            float oldValue = resource.getAmount();
            resource.setAmount(oldValue + delta);
            float newValue = resource.getAmount();
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
            sum += resource.getAmount();
        }
        return sum;
    }

}
