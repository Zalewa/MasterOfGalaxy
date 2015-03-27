package masterofgalaxy.assets.tech;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class TechTier {
    private Map<String, Tech> techs = new LinkedHashMap<String, Tech>();

    public void appendTech(Tech tech) {
        techs.put(tech.getId(), tech);
    }

    public Collection<Tech> getTechs() {
        return techs.values();
    }

    public int size() {
        return techs.size();
    }
}
