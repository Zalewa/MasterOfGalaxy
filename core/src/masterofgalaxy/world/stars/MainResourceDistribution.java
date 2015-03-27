package masterofgalaxy.world.stars;

import masterofgalaxy.AskForFloat;
import masterofgalaxy.gamestate.resources.ResourceDistribution;

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

    ResourceDistribution distribution = new ResourceDistribution();

    public MainResourceDistribution() {
        for (ResourceId id : ResourceId.values()) {
            distribution.addResource(id.name());
        }
    }

    public void setValues(MainResourceDistribution other) {
        distribution.setValues(other.distribution);
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
        distribution.setLocked(id.name(), locked);
    }

    public boolean isLocked(ResourceId id) {
        return distribution.isLocked(id.name());
    }

    public void setMinAmountAsker(ResourceId resourceId, AskForFloat asker) {
        distribution.setMinAmountAsker(resourceId.name(), asker);
    }

    public float getAmount(ResourceId id) {
        return distribution.getAmount(id.name());
    }

    public void setAmount(ResourceId id, float amount) {
        distribution.setAmount(id.name(), amount);
    }

    public void clearAndDistributeElsewhere(ResourceId clearedId, ResourceId targetId) {
        distribution.clearAndDistributeElsewhere(clearedId.name(), targetId.name());
    }
}
