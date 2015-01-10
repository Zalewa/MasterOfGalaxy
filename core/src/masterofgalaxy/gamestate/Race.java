package masterofgalaxy.gamestate;

import masterofgalaxy.assets.i18n.I18NFormsResolver;

public class Race {
    private String name;
    private String localizationBase;
    private String localizationBundleName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocalizationBase(String localizationBase) {
        this.localizationBase = localizationBase;
    }

    public String getLocalizationBase() {
        return localizationBase;
    }

    public void setLocalizationBundleName(String localizationBundleName) {
        this.localizationBundleName = localizationBundleName;
    }

    public String getLocalizationBundleName() {
        return localizationBundleName;
    }

    public String getNameInLocalizedForm(I18NFormsResolver.Form form) {
        return I18NFormsResolver.resolve(localizationBase, localizationBundleName, form);
    }
}
