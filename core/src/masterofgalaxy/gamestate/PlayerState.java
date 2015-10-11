package masterofgalaxy.gamestate;

import masterofgalaxy.assets.tech.TechKnowledge;
import masterofgalaxy.assets.tech.TechTree;
import masterofgalaxy.gamestate.ships.ShipDesign;

import com.badlogic.gdx.utils.Array;

public class PlayerState {
    String name;
    PlayerColor playerColor = PlayerColor.nullColor;
    private String raceName;
    private Array<ShipDesign> shipDesigns = new Array<ShipDesign>();
    private TechKnowledge techKnowledge = new TechKnowledge();
    private TechTree techTree;

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

    public TechKnowledge getTechKnowledge() {
        return techKnowledge;
    }

    public TechTree getTechTree() {
        return techTree;
    }

    public void setTechTree(TechTree techTree) {
        this.techTree = techTree;
    }

    public void set(PlayerState other) {
        setName(other.getName());
        setPlayerColor(other.getPlayerColor());
        setRaceName(other.getRaceName());
        shipDesigns.clear();
        shipDesigns.addAll(other.shipDesigns);
        techKnowledge = other.techKnowledge;
        techTree = other.techTree;
    }

    public Array<ShipDesign> getShipDesigns() {
        return shipDesigns;
    }

    public ShipDesign findShipDesignByName(String name) {
        for (ShipDesign design : shipDesigns) {
            if (design.getName().equals(name)) {
                return design;
            }
        }
        return null;
    }
}
