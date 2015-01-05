package masterofgalaxy.gamestate.savegame;

import masterofgalaxy.ecs.entities.FleetFactory;
import masterofgalaxy.ecs.entities.StarFactory;

public class GameState {
    private WorldState worldState;
    private int starFactoryId = StarFactory.lastId;
    private int fleetFactoryId = FleetFactory.lastId;

    public int getStarFactoryId() {
        return starFactoryId;
    }

    public int getFleetFactoryId() {
        return fleetFactoryId;
    }

    public WorldState getWorldState() {
        return worldState;
    }

    public void setWorldState(WorldState worldState) {
        this.worldState = worldState;
    }
}
