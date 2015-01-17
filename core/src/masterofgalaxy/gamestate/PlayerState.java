package masterofgalaxy.gamestate;

import com.badlogic.gdx.utils.Array;
import masterofgalaxy.gamestate.ships.ShipDesign;

public class PlayerState {
    String name;
    PlayerColor playerColor = PlayerColor.nullColor;
    private String raceName;
    private Array<ShipDesign> shipDesigns = new Array<ShipDesign>();

    public PlayerState() {

    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerColor color) {
        this.playerColor = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getRaceName() {
        return raceName;
    }

    public void set(PlayerState other) {
        setName(other.getName());
        setPlayerColor(other.getPlayerColor());
        setRaceName(other.getRaceName());
        shipDesigns.clear();
        shipDesigns.addAll(other.shipDesigns);
    }

    public Array<ShipDesign> getShipDesigns() {
        return shipDesigns;
    }
}
