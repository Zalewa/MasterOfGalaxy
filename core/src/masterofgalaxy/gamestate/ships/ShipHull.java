package masterofgalaxy.gamestate.ships;

import masterofgalaxy.assets.i18n.I18N;

public class ShipHull {
    public String localizationBundle;
    public String name;
    public float size;
    public float cost;

    public String getLocalizedNameAdjective() {
        return I18N.resolveNamed(localizationBundle, "$hull_" + name + "_adjective");
    }
}
