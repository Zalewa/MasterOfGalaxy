package masterofgalaxy.gamestate.ships;

import masterofgalaxy.assets.actors.ShipModuleType;

public class ShipModule {
    public ShipModuleType type;
    public String name;
    public float size;
    public float cost;
    public float speed;
    public float travelDistanceIncrease;
    public boolean canColonize;

    public String getLocalizedName() {
        return "$" + type.lowerCaseName() + "_" + name;
    }

    public String getLocalizedNameAdjective() {
        return "$" + type.lowerCaseName() + "_" + name + "_adjective";
    }
}
