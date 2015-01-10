package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.RandomUtil;
import masterofgalaxy.ecs.components.*;
import masterofgalaxy.ecs.entities.FleetFactory;
import masterofgalaxy.ecs.entities.StarFactory;
import masterofgalaxy.gamestate.PlayerBuilder;
import masterofgalaxy.gamestate.Race;
import masterofgalaxy.world.stars.Planet;
import masterofgalaxy.world.stars.PlanetClass;
import masterofgalaxy.world.stars.StarClass;
import masterofgalaxy.world.worldbuild.RectangleWorldStarLayout;

import java.text.MessageFormat;
import java.util.Random;

public class WorldBuilder {
    private Rectangle playField;
    private WorldScreen screen;
    private World world;
    private Random random;
    private RectangleWorldStarLayout layout;
    private long seed;
    private int numPlayers;
    private int numUnownedStars;

    public WorldBuilder(WorldScreen screen, RectangleWorldStarLayout layout, long seed) {
        this.seed = seed;
        this.layout = layout;
        this.random = new Random(seed);
        this.screen = screen;
    }

    public World build() {
        world = new World(screen);

        world.setPlayField(playField);
        world.setRaces(new Array<Race>(screen.getGame().getActorAssets().races.races));
        world.setPlayers(new PlayerBuilder(screen.getGame(), seed).randomizePlayers(numPlayers));

        layout.setNumUnownedStars(numUnownedStars);
        layout.setSeed(seed);
        layout.setWorld(world);
        layout.buildStars();

        populateOwnedStars();
        buildFleets();

        return world;
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
            PlayerOwnerComponent starOwner = Mappers.playerOwner.get(star);
            if (starOwner.getOwner().isValid()) {
                Entity fleet = FleetFactory.build(screen.getGame(), screen.getEntityEngine());

                DockComponent dock = screen.getEntityEngine().createComponent(DockComponent.class);
                dock.dockedAt = star;
                fleet.add(dock);

                PlayerOwnerComponent fleetOwner = Mappers.playerOwner.get(fleet);
                fleetOwner.setOwner(starOwner.getOwner());
            }
        }
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
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
}
