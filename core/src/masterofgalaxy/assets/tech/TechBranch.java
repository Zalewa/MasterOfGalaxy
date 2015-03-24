package masterofgalaxy.assets.tech;

import java.util.LinkedHashMap;
import java.util.Map;

public class TechBranch {
    private String id;
    private Map<String, Tech> techs = new LinkedHashMap<String, Tech>();
    private String localizationBundleName;

    public TechBranch() {
    }

    public void appendTech(Tech tech) {
        techs.put(tech.getId(), tech);
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
}
