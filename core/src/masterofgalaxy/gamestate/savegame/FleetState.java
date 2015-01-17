package masterofgalaxy.gamestate.savegame;

import com.badlogic.gdx.utils.Array;
import masterofgalaxy.ecs.components.BodyState;

public class FleetState {
    public String id;
    public String owner;
    public String targetId;
    public String dockedAt;
    public BodyState body;
    public Array<FleetShipsState> ships = new Array<FleetShipsState>();
}
