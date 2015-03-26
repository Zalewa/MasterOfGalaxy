package masterofgalaxy.assets.tech;

import masterofgalaxy.assets.i18n.I18N;

public class Tech {
    private String id;
    private int cost;
    private String parentId;
    private String localizationBundleName;

    public Tech() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLocalizationBundleName() {
        return localizationBundleName;
    }

    public void setLocalizationBundleName(String localizationBundleName) {
        this.localizationBundleName = localizationBundleName;
    }

    @Override
    public String toString() {
        return I18N.resolveNamed(localizationBundleName, id);
    }

    public String getDescription() {
        return I18N.resolveNamed(localizationBundleName, id + "_description");
    }
}
