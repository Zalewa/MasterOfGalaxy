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
import masterofgalaxy.gamestate.Homeworld;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.PlayerBuilder;
import masterofgalaxy.gamestate.Race;
import masterofgalaxy.world.stars.Planet;
import masterofgalaxy.world.stars.PlanetClass;
import masterofgalaxy.world.stars.StarClass;

import java.text.MessageFormat;
import java.util.Random;

public class WorldBuilder {
    private WorldScreen screen;
    private World world;
    private Random random;
    private long seed;

    public WorldBuilder(WorldScreen screen, long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        this.screen = screen;
    }

    public World build() {
        world = new World(screen);
        world.setPlayField(new Rectangle(0.0f, 0.0f, 1200.0f, 1000.0f));
        Array<Race> array = new Array<Race>(screen.getGame().getActorAssets().races.races);
        world.setRaces(array);
        world.setPlayers(new PlayerBuilder(screen.getGame(), seed).randomizePlayers(4));

        createCorners();
        createStars(20);
        assignPlayersToStars();
        buildFleets();

        return world;
    }

    private void assignPlayersToStars() {
        Family family = Family.getFor(StarComponent.class);
        ImmutableArray<Entity> stars = screen.getEntityEngine().getEntitiesFor(family);
        int position = random.nextInt(stars.size() + 1);
        position = Math.max(0, position - world.getPlayers().size);
        for (int i = 0; i < world.getPlayers().size; ++i) {
            Player player = world.getPlayers().get(i);
            Entity star = stars.get(position + i);

            NameComponent name = Mappers.name.get(star);
            Homeworld homeworld = player.getRace().getHomeworld();
            name.setName(homeworld.getName());

            PlayerOwnerComponent owner = Mappers.playerOwner.get(star);
            owner.setOwner(player);

            StarComponent starComponent = Mappers.star.get(star);
            starComponent.klass = screen.getGame().getActorAssets().starClasses.findByType(homeworld.getStarType());
            starComponent.planet = new Planet(screen.getGame().getActorAssets()
                    .planetClasses.findByType(homeworld.getPlanetType()));

            Mappers.spriteRender.get(star).setColor(starComponent.klass.getColor());

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

    private void createCorners() {
        final float margin = 64.0f;

        createStar("BottomLeft", world.getPlayField().getX() + margin, world.getPlayField().getY() + margin);
        createStar("BottomRight", world.getPlayField().getX() + world.getPlayField().getWidth() - margin, world.getPlayField().getY() + margin);
        createStar("TopLeft", world.getPlayField().getX() + margin, world.getPlayField().getY() + world.getPlayField().getHeight() - margin);
        createStar("TopRight", world.getPlayField().getX() + world.getPlayField().getWidth() - margin, world.getPlayField().getY() + world.getPlayField().getHeight() - margin);
    }

    private void createStars(int num) {
        while (num > 0) {
            float x = world.getPlayField().getX() + random.nextFloat() * world.getPlayField().getWidth();
            float y = world.getPlayField().getY() + random.nextFloat() * world.getPlayField().getHeight();
            createStar(MessageFormat.format("Star-{0}", num), x, y);
            --num;
        }
    }

    private void createStar(String name, float x, float y) {
        StarClass klass = screen.getGame().getActorAssets().starClasses.pickRandrom(random);
        PlanetClass planetKlass = screen.getGame().getActorAssets().planetClasses.pickRandomClassForStarType(random, klass.getInternalName());

        float sizeScale = RandomUtil.nextFloat(random, klass.getMinSize(), klass.getMaxSize());

        Entity star = StarFactory.build(screen.getGame(), screen.getEntityEngine());

        BodyComponent body = screen.getGame().getComponentMappers().body.get(star);
        body.setPosition(x, y);
        body.scale(sizeScale);

        NameComponent nameComponent = screen.getGame().getComponentMappers().name.get(star);
        nameComponent.setName(name);

        RenderComponent render = Mappers.spriteRender.get(star);
        render.setColor(klass.getColor());
        render.scaleScale(sizeScale);

        StarComponent starComponent = Mappers.star.get(star);
        starComponent.klass = klass;
        starComponent.planet = new Planet(planetKlass);

        world.addStar(star);
    }
}
