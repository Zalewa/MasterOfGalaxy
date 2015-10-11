package masterofgalaxy.world.worldbuild;

import masterofgalaxy.ecs.components.ColonyComponent;
import masterofgalaxy.ecs.components.FleetComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.PlayerOwnerComponent;
import masterofgalaxy.ecs.components.StarComponent;
import masterofgalaxy.ecs.entities.FleetFactory;
import masterofgalaxy.exceptions.NoDataException;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.PlayerBuilder;
import masterofgalaxy.gamestate.Race;
import masterofgalaxy.gamestate.ships.ShipDesign;
import masterofgalaxy.world.Docker;
import masterofgalaxy.world.World;
import masterofgalaxy.world.WorldScreen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class WorldBuilder {
    private Rectangle playField;
    private WorldScreen screen;
    private World world;
    private RectangleWorldStarLayout layout;
    private long seed;
    private int numRandomPlayers;
    private int numUnownedStars;
    private Array<PlayerSetup> predefinedPlayers;

    public WorldBuilder(WorldScreen screen, RectangleWorldStarLayout layout, long seed) {
        this.seed = seed;
        this.layout = layout;
        this.screen = screen;
    }

    public World build() {
        world = new World(screen);

        world.setPlayField(playField);
        world.setRaces(new Array<Race>(screen.getGame().getActorAssets().races.races));
        world.setPlayers(buildPlayers());
        screen.setCurrentPlayer(world.getPlayers().get(0));

        layout.setNumUnownedStars(numUnownedStars);
        layout.setSeed(seed);
        layout.setWorld(world);
        layout.buildStars();

        populateOwnedStars();
        buildFleets();

        return world;
    }

    private Array<Player> buildPlayers() {
        PlayerBuilder playerBuilder = new PlayerBuilder(screen.getGame(), seed);
        playerBuilder.setPredefinedPlayers(predefinedPlayers);
        playerBuilder.setNumRandomPlayers(numRandomPlayers);
        Array<Player> players = playerBuilder.build();
        return players;
    }

    private void populateOwnedStars() {
        Family family = Family.getFor(PlayerOwnerComponent.class, StarComponent.class);
        ImmutableArray<Entity> stars = screen.getEntityEngine().getEntitiesFor(family);
        for (int i = 0; i < world.getPlayers().size; ++i) {
            Entity star = stars.get(i);
            if (Mappers.playerOwner.get(star).getOwner() == null) {
                continue;
            }

            ColonyComponent colony = screen.getEntityEngine().createComponent(ColonyComponent.class);
            colony.entity = star;
            colony.state.population = 10.0f;
            colony.state.factories = 1.0f;
            star.add(colony);
        }
    }

    private void buildFleets() {
        Family family = Family.getFor(StarComponent.class);
        ImmutableArray<Entity> stars = screen.getEntityEngine().getEntitiesFor(family);
        for (int i = 0; i < stars.size(); ++i) {
            Entity star = stars.get(i);
            Player player = Mappers.playerOwner.get(star).getOwner();
            if (player.isValid()) {
                Entity fleet = FleetFactory.build(screen.getGame(), screen.getEntityEngine());

                Docker.dock(screen, fleet, star);

                PlayerOwnerComponent fleetOwner = Mappers.playerOwner.get(fleet);
                fleetOwner.setOwner(player);

                addShipsToFleet(fleet);
            }
        }
    }

    private void addShipsToFleet(Entity fleet) {
        Player player = Mappers.playerOwner.get(fleet).getOwner();
        FleetComponent fleetComponent = Mappers.fleet.get(fleet);

        FleetComponent.Ship colony = fleetComponent.getOrCreateShipOfDesign(getPlayersColonyShipDesign(player));
        colony.count = 1;

        FleetComponent.Ship scout = fleetComponent.getOrCreateShipOfDesign(getPlayersScoutShipDesign(player));
        scout.count = 3;
    }

    private ShipDesign getPlayersColonyShipDesign(Player player) {
        for (ShipDesign design : player.getState().getShipDesigns()) {
            if (design.canColonize()) {
                return design;
            }
        }
        throw new NoDataException("no default colony ship for player " + player.getName());
    }

    private ShipDesign getPlayersScoutShipDesign(Player player) {
        for (ShipDesign design : player.getState().getShipDesigns()) {
            if (design.getTravelDistanceIncrease() > 0.0f) {
                return design;
            }
        }
        throw new NoDataException("no default scout ship for player " + player.getName());
    }

    public int getNumRandomPlayers() {
        return numRandomPlayers;
    }

    public void setNumRandomPlayers(int numPlayers) {
        this.numRandomPlayers = numPlayers;
    }

    public Rectangle getPlayField() {
        return playField;
    }

    public void setPlayField(Rectangle playField) {
        this.playField = playField;
    }

    public int getNumUnownedStars() {
        return numUnownedStars;
    }

    public void setNumUnownedStars(int numUnownedStars) {
        this.numUnownedStars = numUnownedStars;
    }

    public void setPredefinedPlayers(Array<PlayerSetup> players) {
        this.predefinedPlayers = players;
    }
}
