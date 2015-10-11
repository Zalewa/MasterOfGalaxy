package masterofgalaxy.world.worldbuild;

import masterofgalaxy.gamestate.PlayerColor;
import masterofgalaxy.gamestate.Race;

public class PlayerSetup {
    private String name;
    private PlayerColor color;
    private Race race;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
