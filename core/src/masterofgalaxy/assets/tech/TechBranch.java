package masterofgalaxy.assets.tech;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TechBranch {
    private String id;
    private List<TechTier> tiers = new LinkedList<TechTier>();
    private String localizationBundleName;

    public TechBranch() {
    }

    public void appendTechTier(TechTier techTier) {
        tiers.add(techTier);
    }

    public List<Tech> getResearchableTechs(TechKnowledge knowledge) {
        List<Tech> result = new LinkedList<Tech>();
        for (TechTier tier : tiers) {
            result.addAll(getUnresearchedTechsFromTier(tier, knowledge));
            if (!isEnoughKnowledgeLearnedInTier(tier, knowledge)) {
                break;
            }
        }
        return result;
    }

    private List<Tech> getUnresearchedTechsFromTier(TechTier tier, TechKnowledge knowledge) {
        List<Tech> result = new LinkedList<Tech>();
        for (Tech tech : tier.getTechs()) {
            if (!knowledge.hasTech(this, tech)) {
                result.add(tech);
            }
        }
        return result;
    }

    private boolean isEnoughKnowledgeLearnedInTier(TechTier tier, TechKnowledge knowledge) {
        int count = 0;
        for (Tech tech : tier.getTechs()) {
            if (knowledge.hasTech(this, tech)) {
                count += 1;
            }
        }
        if (tier.size() > 1) {
            return count + 1 >= tier.size();
        } else {
            return count == tier.size();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalizationBundleName() {
        return localizationBundleName;
    }

    public void setLocalizationBundleName(String localizationBundleName) {
        this.localizationBundleName = localizationBundleName;
    }

    public Tech getTech(String name) {
        for (Tech tech : getTechs()) {
            if (tech.getId().equals(name)) {
                return tech;
            }
        }
        return null;
    }

    public Collection<Tech> getTechs() {
        List<Tech> result = new LinkedList<Tech>();
        for (TechTier tier : tiers) {
            result.addAll(tier.getTechs());
        }
        return result;
    }
}
