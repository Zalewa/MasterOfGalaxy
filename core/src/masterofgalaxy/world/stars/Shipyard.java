package masterofgalaxy.world.stars;

import masterofgalaxy.gamestate.ships.ShipDesign;

public class Shipyard {
    public ShipDesign constructedShip;
    public float investment;

    public void reset() {
        constructedShip = null;
        investment = 0.0f;
    }

    public String getConstructedShipName() {
        return constructedShip != null ? constructedShip.getName() : "";
    }

    public float getConstructedShipProgress() {
        return constructedShip != null ? investment / constructedShip.getCost() : 0.0f;
    }
}
