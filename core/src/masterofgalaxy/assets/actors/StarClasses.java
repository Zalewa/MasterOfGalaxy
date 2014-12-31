package masterofgalaxy.assets.actors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import masterofgalaxy.exceptions.InvalidInputTypeException;
import masterofgalaxy.world.stars.PlanetClass;
import masterofgalaxy.world.stars.StarClass;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class StarClasses {
    private Map<String, StarClass> stars = new LinkedHashMap<String, StarClass>();

    public StarClasses() {
    }

    public void load(FileHandle file) {
        JsonReader reader = new JsonReader();
        JsonValue value = reader.parse(file);
        JsonValue stars = value.get("stars");
        if (stars == null || !stars.isObject()) {
            throw new InvalidInputTypeException(file.path() + " is not a stars file");
        }
        for (int i = 0; i < stars.size; ++i) {
            JsonValue star = stars.get(i);
            String name = star.name();

            StarClass klass = new StarClass();
            klass.setName(star.getString("typeName"));
            klass.setInternalName(name);
            klass.setColor(Color.valueOf(star.getString("color")));
            klass.setMaxSize(star.getFloat("maxSize"));
            klass.setMinSize(star.getFloat("minSize"));

            this.stars.put(name, klass);
        }
    }

    public StarClass pickRandrom(Random random) {
        StarClass[] klasses = stars.values().toArray(new StarClass[stars.size()]);
        return klasses[random.nextInt(klasses.length)];
    }
}
