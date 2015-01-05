package masterofgalaxy.gamestate.savegame;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.PlayerState;

public class WorldState {
    public Rectangle playField;
    public Array<PlayerState> players;
    public Array<StarState> stars;
    public Array<FleetState> fleets;
}
