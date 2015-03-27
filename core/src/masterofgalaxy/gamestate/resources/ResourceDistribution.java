package masterofgalaxy.gamestate.resources;

import java.util.LinkedHashMap;
import java.util.Map;

import masterofgalaxy.AskForFloat;

public class ResourceDistribution {
    public final static float pool = 1.0f;

    private Map<String, Resource> distribution = new LinkedHashMap<String, Resource>();

    public ResourceDistribution() {
    }

    public void addResource(String id) {
        distribution.put(id, new Resource());
    }

    public void setValues(ResourceDistribution other) {
        distribution.clear();
        for (String key : other.distribution.keySet()) {
            addResource(key);
            distribution.get(key).setValues(other.distribution.get(key));
        }
    }

    public void setLocked(String id, boolean locked) {
        distribution.get(id).locked = locked;
    }

    public boolean isLocked(String id) {
        return distribution.get(id).locked;
    }

    public void setMinAmountAsker(String resourceId, AskForFloat asker) {
        distribution.get(resourceId).minAmountAsker = asker;
    }

    public float getAmount(String id) {
        return distribution.get(id).getAmount();
    }

    public void setAmount(String id, float amount) {
        Resource resource = distribution.get(id);
        resource.setAmount(amount);
        normalizeDistribution(id);
    }

    public int size() {
        return distribution.size();
    }

    public void clearAndDistributeElsewhere(String clearedId, String targetId) {
        Resource source = distribution.get(clearedId);
        Resource target = distribution.get(targetId);
        float amount = source.getAmount();
        source.setAmount(0.0f);
        float delta = amount - source.getAmount();
        target.setAmount(target.getAmount() + delta);
    }

    private void normalizeDistribution(String id) {
        normalizeDistribution(id, pool - getSum());
    }

    private void normalizeDistribution(String id, float delta) {
        int idx = indexOfResourceId(id);
        for (int i = 1; i <= distribution.size(); ++i) {
            int nextIdx = (idx + i) % distribution.size();
            String nextId = getKeyAtIndex(nextIdx);
            Resource resource = distribution.get(nextId);
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

    private String getKeyAtIndex(int nextIdx) {
        return distribution.keySet().toArray(new String[0])[nextIdx];
    }

    private int indexOfResourceId(String id) {
        for (int i = 0; i < distribution.size(); ++i) {
            if (getKeyAtIndex(i).equals(id)) {
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

    public void equalize() {
        if (size() == 0) {
            return;
        }
        for (Resource resource : distribution.values()) {
            resource.setAmount(pool / size());
        }
        normalizeDistribution(distribution.keySet().iterator().next());
    }
}
