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
}
