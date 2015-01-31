package masterofgalaxy.gamestate.savegame;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.gamestate.PlayerState;
import masterofgalaxy.gamestate.Race;

public class WorldState {
    public int turn;
    public Rectangle playField;
    public Array<PlayerState> players;
    public String currentPlayer;
    public Array<StarState> stars;
    public Array<FleetState> fleets;
    public Array<Race> races;
    public Array<ColonyPersistence> colonies;
}
