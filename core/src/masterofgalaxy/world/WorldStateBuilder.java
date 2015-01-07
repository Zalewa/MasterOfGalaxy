package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.ecs.components.*;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.PlayerState;
import masterofgalaxy.gamestate.savegame.FleetState;
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
        state.turn = worldScreen.getTurn();
        state.playField = worldScreen.getWorld().getPlayField();
        state.players = getPlayers();
        state.stars = getStars();
        state.fleets = getFleets();
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
            state.id = Mappers.id.get(star).id;
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

    private Array<FleetState> getFleets() {
        Array<FleetState> states = new Array<FleetState>();
        ImmutableArray<Entity> fleets = worldScreen.getEntityEngine().getEntitiesFor(Family.getFor(FleetComponent.class));
        for (int i = 0; i < fleets.size(); ++i) {
            Entity fleet = fleets.get(i);
            FleetState state = new FleetState();
            state.id = Mappers.id.get(fleet).id;
            state.body = Mappers.body.get(fleet).getState();

            Player owner = Mappers.playerOwner.get(fleet).getOwner();
            if (owner.isValid()) {
                state.owner = owner.getName();
            }

            DockComponent dock = Mappers.dock.get(fleet);
            if (dock != null) {
                state.dockedAt = Mappers.id.get(dock.dockedAt).id;
            }

            EntityTargetComponent target = Mappers.entityTarget.get(fleet);
            if (target.target != null) {
                state.targetId = Mappers.id.get(target.target).id;
            }
            states.add(state);
        }
        return states;
    }
}
