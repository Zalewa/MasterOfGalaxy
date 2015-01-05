package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import masterofgalaxy.ecs.components.BodyComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.RenderComponent;
import masterofgalaxy.ecs.entities.StarFactory;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.PlayerBuilder;
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
    }

    private void restoreWorld() {
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
}
