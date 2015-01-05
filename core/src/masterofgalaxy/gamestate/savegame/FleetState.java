package masterofgalaxy.gamestate.savegame;

import masterofgalaxy.ecs.components.BodyState;
import masterofgalaxy.ecs.components.FleetComponent;
import masterofgalaxy.ecs.components.StarComponent;

public class FleetState {
    public String id;
    public String owner;
    public String targetId;
    public String dockedAt;
    public BodyState body;
}
