package masterofgalaxy.assets.tech;

import masterofgalaxy.assets.i18n.I18NJsonReader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class TechBranchParser {
    private String localizationBundleName;

    public TechBranch load(FileHandle file) {
        TechBranch techBranch = new TechBranch();

        JsonReader reader = new JsonReader();
        JsonValue jsonRoot = reader.parse(file);

        localizationBundleName = I18NJsonReader.loadNamedBundle(jsonRoot, file.type());

        techBranch.setId(jsonRoot.getString("id"));
        techBranch.setLocalizationBundleName(localizationBundleName);

        JsonValue tierDefs = jsonRoot.get("tiers");

        for (int i = 0; i < tierDefs.size; ++i) {
            JsonValue techTierDef = tierDefs.get(i);
            TechTier techTier = parseTechTier(techTierDef);
            techBranch.appendTechTier(techTier);
        }

        return techBranch;
    }

    private TechTier parseTechTier(JsonValue techTierDef) {
        TechTier tier = new TechTier();
        JsonValue techDefs = techTierDef.get("techs");
        for (int i = 0; i < techDefs.size; ++i) {
            tier.appendTech(parseTech(techDefs.get(i)));
        }
        return tier;
    }

    private Tech parseTech(JsonValue techDef) {
        Tech tech = new Tech();
        tech.setId(techDef.getString("id"));
        tech.setLocalizationBundleName(localizationBundleName);
        tech.setCost(techDef.getInt("cost"));
        return tech;
    }
}
