package masterofgalaxy.world.worldbuild;

import com.badlogic.ashley.core.Entity;
import masterofgalaxy.MogGame;
import masterofgalaxy.RandomUtil;
import masterofgalaxy.ecs.components.*;
import masterofgalaxy.ecs.entities.StarFactory;
import masterofgalaxy.gamestate.Homeworld;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.world.World;
import masterofgalaxy.world.WorldScreen;
import masterofgalaxy.world.stars.Planet;
import masterofgalaxy.world.stars.PlanetClass;
import masterofgalaxy.world.stars.StarClass;

import java.util.Random;

public class StarBuilder {
    private World world;
    private MogGame game;
    private WorldScreen screen;
    private Random random;
    private int nameIdx = 0;

    public StarBuilder(WorldScreen screen, World world, Random random) {
        this.game = screen.getGame();
        this.screen = screen;
        this.world = world;
        this.random = random;
    }

    public Entity createRandomStar(StarClass starKlass, float x, float y) {
        PlanetClass planetKlass = game.getActorAssets().planetClasses.pickRandomClassForStarType(random, starKlass.getInternalName());

        float sizeScale = randomizeStarScale(starKlass);

        Entity star = StarFactory.build(game, screen.getEntityEngine());

        applyBodyAndAppearance(star, starKlass, x, y, sizeScale);
        applyNameToEntity(star, nextRandomName());
        applyStarComponent(star, starKlass, planetKlass);

        world.addStar(star);
        return star;
    }

    public Entity createHomeworld(Player player, float x, float y) {
        Homeworld homeworld = player.getRace().getHomeworld();

        StarClass starKlass = game.getActorAssets().starClasses.findByType(homeworld.getStarType());
        PlanetClass planetKlass = game.getActorAssets().planetClasses.findByType(homeworld.getPlanetType());
        Entity star = StarFactory.build(game, screen.getEntityEngine());

        float sizeScale = randomizeStarScale(starKlass);

        applyBodyAndAppearance(star, starKlass, x, y, sizeScale);
        applyNameToEntity(star, homeworld.getName());
        applyStarComponent(star, starKlass, planetKlass);

        Mappers.playerOwner.get(star).setOwner(player);

        world.addStar(star);
        return star;
    }

    private float randomizeStarScale(StarClass starKlass) {
        return RandomUtil.nextFloat(random, starKlass.getMinSize(), starKlass.getMaxSize());
    }

    private void applyBodyAndAppearance(Entity star, StarClass starKlass, float x, float y, float sizeScale) {
        BodyComponent body = Mappers.body.get(star);
        body.setPosition(x, y);
        body.scale(sizeScale);

        RenderComponent render = Mappers.spriteRender.get(star);
        render.setColor(starKlass.getColor());
        render.scaleScale(sizeScale);
    }

    private void applyStarComponent(Entity star, StarClass klass, PlanetClass planetKlass) {
        StarComponent starComponent = Mappers.star.get(star);
        starComponent.klass = klass;
        starComponent.planet = new Planet(planetKlass);
    }

    private void applyNameToEntity(Entity entity, String name) {
        Mappers.name.get(entity).setName(name);
    }

    private String nextRandomName() {
        return "Star-" + (nameIdx++);
    }
}
