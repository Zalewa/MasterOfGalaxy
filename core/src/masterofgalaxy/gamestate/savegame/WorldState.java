package masterofgalaxy.gamestate.savegame;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.gamestate.PlayerState;

public class WorldState {
    public int turn;
    public Rectangle playField;
    public Array<PlayerState> players;
    public Array<StarState> stars;
    public Array<FleetState> fleets;
}
