package masterofgalaxy.assets.actors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import masterofgalaxy.assets.i18n.I18NJsonReader;
import masterofgalaxy.exceptions.InvalidInputTypeException;
import masterofgalaxy.world.stars.StarClass;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class StarClasses {
    private Map<String, StarClass> stars = new LinkedHashMap<String, StarClass>();
    private String localizationBundleName;

    public StarClasses() {
    }

    public void load(FileHandle file) {
        JsonReader reader = new JsonReader();
        JsonValue jsonRoot = reader.parse(file);
        localizationBundleName = I18NJsonReader.loadNamedBundle(jsonRoot, file.type());
        JsonValue stars = jsonRoot.get("stars");
        if (stars == null || !stars.isObject()) {
            throw new InvalidInputTypeException(file.path() + " is not a stars file");
        }
        parseStars(stars);
    }

    private void parseStars(JsonValue stars) {
        for (int i = 0; i < stars.size; ++i) {
            JsonValue star = stars.get(i);
            String name = star.name();
            StarClass klass = parseStar(name, star);
            this.stars.put(name, klass);
        }
    }

    private StarClass parseStar(String name, JsonValue star) {
        StarClass klass = new StarClass();
        klass.setName(star.getString("typeName"));
        klass.setInternalName(name);
        klass.setLocalizationBundleName(localizationBundleName);
        klass.setColor(Color.valueOf(star.getString("color")));
        klass.setMaxSize(star.getFloat("maxSize"));
        klass.setMinSize(star.getFloat("minSize"));
        return klass;
    }

    public StarClass pickRandrom(Random random) {
        StarClass[] klasses = stars.values().toArray(new StarClass[stars.size()]);
        return klasses[random.nextInt(klasses.length)];
    }
}
