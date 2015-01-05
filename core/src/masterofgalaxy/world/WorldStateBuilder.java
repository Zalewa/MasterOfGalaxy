package masterofgalaxy.world;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.StarComponent;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.PlayerState;
import masterofgalaxy.gamestate.savegame.StarState;
import masterofgalaxy.gamestate.savegame.WorldState;

public class WorldStateBuilder {
    private WorldScreen worldScreen;
    private WorldState state;

    public WorldStateBuilder(WorldScreen worldScreen) {
        this.worldScreen = worldScreen;
    }

    public WorldState build() {
        state = new WorldState();
        state.playField = worldScreen.getWorld().getPlayField();
        state.players = getPlayers();
        state.stars = getStars();
        return state;
    }

    private Array<PlayerState> getPlayers() {
        Array<PlayerState> result = new Array<PlayerState>();
        for (Player player : worldScreen.getWorld().getPlayers()) {
            result.add(player.getState());
        }
        return result;
    }

    private Array<StarState> getStars() {
        Array<StarState> states = new Array<StarState>();
        ImmutableArray<Entity> stars = worldScreen.getEntityEngine().getEntitiesFor(Family.getFor(StarComponent.class));
        for (int i = 0; i < stars.size(); ++i) {
            Entity star = stars.get(i);
            StarState state = new StarState();
            state.body = Mappers.body.get(star).getState();

            Player owner = Mappers.playerOwner.get(star).getOwner();
            if (owner.isValid()) {
                state.owner = owner.getName();
            }
            state.star = Mappers.star.get(star);
            state.name = Mappers.name.get(star).getName();
            states.add(state);
        }
        return states;
    }
}
