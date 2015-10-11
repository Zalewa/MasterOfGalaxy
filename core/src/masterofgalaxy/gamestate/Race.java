package masterofgalaxy.gamestate;

import masterofgalaxy.assets.i18n.I18NFormsResolver;

public class Race {
    private String name;
    private String localizationBase;
    private String localizationBundleName;
    private float populationGrowthRate = 1.0f;
    private float productionRate = 1.0f;
    private float researchRate;
    private Homeworld homeworld;

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

    public float getPopulationGrowthRate() {
        return populationGrowthRate;
    }

    public void setPopulationGrowthRate(float populationGrowthRate) {
        this.populationGrowthRate = populationGrowthRate;
    }

    public float getProductionRate() {
        return productionRate;
    }

    public void setProductionRate(float productionRate) {
        this.productionRate = productionRate;
    }

    public void setResearchRate(float researchRate) {
        this.researchRate = researchRate;
    }

    public float getResearchRate() {
        return researchRate;
    }

    public Homeworld getHomeworld() {
        return homeworld;
    }

    public void setHomeworld(Homeworld homeworld) {
        this.homeworld = homeworld;
    }

    @Override
    public String toString() {
        return I18NFormsResolver.resolve(localizationBase,
                localizationBundleName, I18NFormsResolver.Form.Plural);
    }
}
