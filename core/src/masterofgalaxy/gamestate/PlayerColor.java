package masterofgalaxy.gamestate;

import com.badlogic.gdx.graphics.Color;

import masterofgalaxy.assets.i18n.I18N;

public class PlayerColor {
    public final static PlayerColor nullColor;

    static {
        nullColor = new PlayerColor(Color.GRAY);
        nullColor.setName("");
    }

    private String name;
    private Color color = new Color();
    private String localizationBundleName;

    public PlayerColor() {

    }

    public PlayerColor(Color color) {
        this.color.set(color);
    }

    public String getName() {
        return name;
    }

    public String getLocalizedName() {
        return I18N.resolveNamed(localizationBundleName, name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalizationBundleName() {
        return localizationBundleName;
    }

    public void setLocalizationBundleName(String localizationBundleName) {
        this.localizationBundleName = localizationBundleName;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public void set(PlayerColor color) {
        setName(color.getName());
        setLocalizationBundleName(color.getLocalizationBundleName());
        setColor(color.getColor());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime
                * result
                + ((localizationBundleName == null) ? 0
                        : localizationBundleName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerColor other = (PlayerColor) obj;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (localizationBundleName == null) {
            if (other.localizationBundleName != null)
                return false;
        } else if (!localizationBundleName.equals(other.localizationBundleName))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
