package masterofgalaxy.world.stars;

import com.badlogic.gdx.graphics.Color;
import masterofgalaxy.assets.i18n.I18N;

public class StarClass {
    public static final StarClass nullKlass;

    static {
        nullKlass = new StarClass();
        nullKlass.valid = false;
    }

    private String name = "null";
    private String internalName = "null";
    private Color color = Color.WHITE.cpy();
    private float minSize = 1.0f;
    private float maxSize = 1.0f;
    private String localizationBundleName;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(float maxSize) {
        this.maxSize = maxSize;
    }

    public float getMinSize() {
        return minSize;
    }

    public void setMinSize(float minSize) {
        this.minSize = minSize;
    }

    public String getLocalizationBundleName() {
        return localizationBundleName;
    }

    public void setLocalizationBundleName(String localizationBundleName) {
        this.localizationBundleName = localizationBundleName;
    }

    public String getLocalizedName() {
        return I18N.resolveNamed(localizationBundleName, name);
    }

    public boolean isValid() {
        return valid;
    }
}
