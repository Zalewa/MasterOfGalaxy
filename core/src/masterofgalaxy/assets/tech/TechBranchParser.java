package masterofgalaxy.assets.tech;

import masterofgalaxy.assets.i18n.I18NJsonReader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class TechBranchParser {
    private String localizationBundleName;

    public TechBranch load(FileHandle file) {
        TechBranch techTree = new TechBranch();

        JsonReader reader = new JsonReader();
        JsonValue jsonRoot = reader.parse(file);

        localizationBundleName = I18NJsonReader.loadNamedBundle(jsonRoot, file.type());

        techTree.setId(jsonRoot.getString("id"));
        techTree.setLocalizationBundleName(localizationBundleName);

        JsonValue techs = jsonRoot.get("tech");

        for (int i = 0; i < techs.size; ++i) {
            JsonValue techDef = techs.get(i);
            Tech tech = parseTech(techDef);
            techTree.appendTech(tech);
        }

        return techTree;
    }

    private Tech parseTech(JsonValue techDef) {
        Tech tech = new Tech();
        tech.setId(techDef.getString("id"));
        tech.setLocalizationBundleName(localizationBundleName);
        tech.setCost(techDef.getInt("cost"));
        tech.setParentId(techDef.getString("requires"));
        return tech;
    }
}
