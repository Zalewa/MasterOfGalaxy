package masterofgalaxy.gamestate.savegame;

import masterofgalaxy.ecs.entities.StarFactory;

public class GameState {
    private WorldState worldState;
    private int starFactoryId = StarFactory.lastId;

    public int getStarFactoryId() {
        return starFactoryId;
    }

    public WorldState getWorldState() {
        return worldState;
    }

    public void setWorldState(WorldState worldState) {
        this.worldState = worldState;
    }
}
