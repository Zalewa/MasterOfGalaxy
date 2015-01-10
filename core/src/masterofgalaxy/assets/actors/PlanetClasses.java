package masterofgalaxy.assets.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import masterofgalaxy.ChanceRange;
import masterofgalaxy.assets.i18n.I18NJsonReader;
import masterofgalaxy.exceptions.InvalidInputTypeException;
import masterofgalaxy.exceptions.NoDataException;
import masterofgalaxy.world.stars.PlanetClass;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class PlanetClasses {
    private Map<String, PlanetClass> planets = new LinkedHashMap<String, PlanetClass>();
    private String localizationBundleName;

    public PlanetClasses() {
    }

    public void load(FileHandle file) {
        JsonReader reader = new JsonReader();
        JsonValue jsonRoot = reader.parse(file);
        localizationBundleName = I18NJsonReader.loadNamedBundle(jsonRoot, file.type());
        JsonValue planets = jsonRoot.get("planets");
        if (planets == null || !planets.isObject()) {
            throw new InvalidInputTypeException(file.path() + " is not a planets file");
        }
        for (int i = 0; i < planets.size; ++i) {
            JsonValue planet = planets.get(i);
            String name = planet.name();

            PlanetClass klass = new PlanetClass();
            klass.setName(planet.getString("typeName"));
            klass.setInternalName(name);
            klass.setLocalizationBundleName(localizationBundleName);
            klass.setTextureName(planet.getString("textureName"));

            JsonValue starChances = planet.get("starChance");
            loadStarChances(klass, starChances);

            this.planets.put(name, klass);
        }
    }

    private void loadStarChances(PlanetClass klass, JsonValue starChances) {
        klass.setDefaultStarChance(starChances.getFloat("default"));
        klass.getStarChances().clear();
        JsonValue preciseChances = starChances.get("stars");
        if (preciseChances != null) {
            loadPreciseStarChances(klass, preciseChances);
        }
    }

    private void loadPreciseStarChances(PlanetClass klass, JsonValue preciseChances) {
        for (int i = 0; i < preciseChances.size; ++i) {
            JsonValue chance = preciseChances.get(i);
            klass.getStarChances().put(chance.name, chance.asFloat());
        }
    }

    public void loadTextures(AssetManager asset) {
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.magFilter = Texture.TextureFilter.Nearest;
        param.minFilter = Texture.TextureFilter.Nearest;

        for (PlanetClass planet : planets.values()) {
            if (!planet.getTextureName().trim().isEmpty()) {
                asset.load(planet.getTextureName(), Texture.class, param);
            }
        }
    }

    public PlanetClass pickRandomClassForStarType(Random random, String starType) {
        PlanetClass[] klasses = planets.values().toArray(new PlanetClass[planets.size()]);
        Array<ChanceRange<PlanetClass>> chances = new Array<ChanceRange<PlanetClass>>();
        float chancePeriod = 0.0f;
        for (PlanetClass klass : klasses) {
            float chance = klass.getChanceForStarType(starType);
            if (Float.compare(chance, 0.0f) != 0) {
                chances.add(new ChanceRange<PlanetClass>(klass, chancePeriod, chancePeriod + chance));
                chancePeriod += chance;
            }
        }

        if (chances.size == 0) {
            throw new NoDataException("no valid planet types found for star type: " + starType);
        }

        float period = random.nextFloat() * chances.get(chances.size - 1).maxChance;
        for (ChanceRange<PlanetClass> chance : chances) {
            if (period >= chance.minChance && period < chance.maxChance) {
                return chance.item;
            }
        }
        return chances.get(0).item;
    }

    public PlanetClass findByType(String planetType) {
        return planets.get(planetType);
    }
}
