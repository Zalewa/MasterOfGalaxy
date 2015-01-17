package masterofgalaxy.gamestate.savegame;

import masterofgalaxy.ecs.components.BodyState;

public class FleetState {
    public String id;
    public String owner;
    public String targetId;
    public String dockedAt;
    public BodyState body;
    public FleetShipsState ships;
}
