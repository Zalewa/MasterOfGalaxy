package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import masterofgalaxy.ecs.components.*;
import masterofgalaxy.ecs.entities.FleetFactory;
import masterofgalaxy.ecs.entities.StarFactory;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.PlayerBuilder;
import masterofgalaxy.gamestate.savegame.FleetState;
import masterofgalaxy.gamestate.savegame.StarState;
import masterofgalaxy.gamestate.savegame.WorldState;

public class WorldStateRestorer {
    private final WorldScreen worldScreen;
    private WorldState worldState;

    public WorldStateRestorer(WorldScreen worldScreen) {
        this.worldScreen = worldScreen;
    }

    public void restore(WorldState state) {
        this.worldState = state;
        restoreWorld();
        restorePlayers();
        restoreStars();
        restoreFleets();
    }

    private void restoreWorld() {
        worldScreen.setTurn(worldState.turn);
        worldScreen.getWorld().setPlayField(worldState.playField);
    }

    private void restorePlayers() {
        worldScreen.getWorld().setPlayers(PlayerBuilder.fromStates(worldState.players));
    }

    private void restoreStars() {
        for (StarState star : worldState.stars) {
            restoreStar(star);
        }
    }

    private void restoreStar(StarState starState) {
        Player owner = worldScreen.getWorld().findPlayerByName(starState.owner);
        Entity star = StarFactory.build(worldScreen.getGame(), worldScreen.getEntityEngine());

        Mappers.id.get(star).id = starState.id;
        Mappers.playerOwner.get(star).setOwner(owner);
        Mappers.body.get(star).setState(starState.body);
        Mappers.star.get(star).setState(starState.star);
        Mappers.name.get(star).setName(starState.name);

        RenderComponent render = Mappers.spriteRender.get(star);
        render.setColor(starState.star.klass.getColor());
        render.setScaleToSize(starState.body.getSize());
    }

    private void restoreFleets() {
        for (FleetState fleet : worldState.fleets) {
            restoreFleet(fleet);
        }
    }

    private void restoreFleet(FleetState fleetState) {
        Player owner = worldScreen.getWorld().findPlayerByName(fleetState.owner);
        Entity fleet = FleetFactory.build(worldScreen.getGame(), worldScreen.getEntityEngine());

        Mappers.id.get(fleet).id = fleetState.id;
        Mappers.playerOwner.get(fleet).setOwner(owner);
        Mappers.body.get(fleet).setState(fleetState.body);

        if (fleetState.dockedAt != null) {
            Entity entity = worldScreen.getWorld().findEntityById(fleetState.dockedAt);
            if (entity != null) {
                DockComponent dockComponent = worldScreen.getEntityEngine().createComponent(DockComponent.class);
                dockComponent.dockedAt = entity;
                fleet.add(dockComponent);
            }
        }

        if (fleetState.targetId != null) {
            Entity entity = worldScreen.getWorld().findEntityById(fleetState.targetId);
            if (entity != null) {
                EntityTargetComponent target = Mappers.entityTarget.get(fleet);
                target.target = entity;
            }
        }
    }
}
