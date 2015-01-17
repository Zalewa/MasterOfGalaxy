package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.assets.actors.Races;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.ecs.components.*;
import masterofgalaxy.ecs.entities.FleetFactory;
import masterofgalaxy.ecs.entities.StarFactory;
import masterofgalaxy.exceptions.SavedGameException;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.PlayerBuilder;
import masterofgalaxy.gamestate.Race;
import masterofgalaxy.gamestate.savegame.*;
import masterofgalaxy.gamestate.ships.ShipDesign;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldStateRestorer {
    private final WorldScreen worldScreen;
    private Logger logger = Logger.getLogger(WorldStateRestorer.class.getName());
    private WorldState worldState;

    public WorldStateRestorer(WorldScreen worldScreen) {
        this.worldScreen = worldScreen;
    }

    public void restore(WorldState state) throws SavedGameException {
        this.worldState = state;
        restoreWorld();
        restoreRaces();
        restorePlayers();
        restoreStars();
        restoreColonies();
        restoreFleets();
    }

    private void restoreRaces() {
        worldScreen.getWorld().setRaces(worldState.races);
    }

    private void restoreWorld() {
        worldScreen.setTurn(worldState.turn);
        worldScreen.getWorld().setPlayField(worldState.playField);
    }

    private void restorePlayers() throws SavedGameException {
        worldScreen.getWorld().setPlayers(PlayerBuilder.fromStates(worldState.players));
        for (Player player : worldScreen.getWorld().getPlayers()) {
            Race race = Races.findRaceByName(worldScreen.getWorld().getRaces(), player.getState().getRaceName());
            if (race == null) {
                throw new SavedGameException(I18N.resolve("$raceCannotBeFoundInSaveGame",
                        player.getState().getRaceName(), player.getName()));
            }
            player.setRace(race);
        }
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

    private void restoreColonies() {
        for (ColonyPersistence colony : worldState.colonies) {
            restoreColony(colony);
        }
    }

    private void restoreColony(ColonyPersistence colonyState) {
        Entity star = worldScreen.getWorld().findEntityById(colonyState.entityId);
        if (star != null) {
            ColonyComponent colony = worldScreen.getEntityEngine().createComponent(ColonyComponent.class);
            colony.entity = star;
            colony.state.set(colonyState.state);
            star.add(colony);
        } else {
            logger.log(Level.SEVERE, MessageFormat.format("parent entity '{0}' for colony not found", colonyState.entityId));
        }
    }

    private void restoreFleets() throws SavedGameException {
        for (FleetState fleet : worldState.fleets) {
            restoreFleet(fleet);
        }
    }

    private void restoreFleet(FleetState fleetState) throws SavedGameException {
        Player owner = worldScreen.getWorld().findPlayerByName(fleetState.owner);
        Entity fleet = FleetFactory.build(worldScreen.getGame(), worldScreen.getEntityEngine());

        Mappers.id.get(fleet).id = fleetState.id;
        Mappers.playerOwner.get(fleet).setOwner(owner);
        Mappers.body.get(fleet).setState(fleetState.body);

        if (fleetState.dockedAt != null) {
            Entity entity = worldScreen.getWorld().findEntityById(fleetState.dockedAt);
            if (entity != null) {
                Docker.dock(worldScreen, fleet, entity);
            }
        }

        if (fleetState.targetId != null) {
            Entity entity = worldScreen.getWorld().findEntityById(fleetState.targetId);
            if (entity != null) {
                EntityTargetComponent target = Mappers.entityTarget.get(fleet);
                target.target = entity;
            }
        }

        restoreFleetShips(fleet, fleetState.ships);
    }

    private void restoreFleetShips(Entity fleet, Array<FleetShipsState> shipsStates) throws SavedGameException {
        Player player = Mappers.playerOwner.get(fleet).getOwner();
        FleetComponent fleetComponent = Mappers.fleet.get(fleet);
        for (FleetShipsState shipsState : shipsStates) {
            ShipDesign design = player.getState().findShipDesignByName(shipsState.designName);
            if (design == null) {
                throw new SavedGameException(I18N.resolve("$saveGamePlayerShipDesignMissing",
                        player.getName(), shipsState.designName));
            }
            FleetComponent.Ship fleetShip = fleetComponent.getOrCreateShipOfDesign(design);
            fleetShip.count = shipsState.count;
        }
    }
}
