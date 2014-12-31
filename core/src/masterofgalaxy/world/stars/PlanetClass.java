package masterofgalaxy.world.stars;

import com.badlogic.gdx.graphics.Texture;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlanetClass {
    public final static PlanetClass nullKlass;

    static {
        nullKlass = new PlanetClass();
        nullKlass.valid = false;
    }

    private String name = "null";
    private String internalName = "null";
    private String textureName = "";
    private Texture texture;
    private float defaultStarChance = 0.0f;
    private Map<String, Float> starChances = new LinkedHashMap<String, Float>();
    private boolean valid = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public String getTextureName() {
        return textureName;
    }

    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }

    public boolean isValid() {
        return valid;
    }

    public float getDefaultStarChance() {
        return defaultStarChance;
    }

    public void setDefaultStarChance(float defaultStarChance) {
        this.defaultStarChance = defaultStarChance;
    }

    public Map<String, Float> getStarChances() {
        return starChances;
    }

    public float getChanceForStarType(String starTypeName) {
        Float precise = starChances.get(starTypeName);
        return precise != null ? precise : defaultStarChance;
    }
}
