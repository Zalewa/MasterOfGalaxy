package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.ecs.components.IdComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.Race;

public class World {
    private WorldScreen screen;
    private Array<Entity> entities = new Array<Entity>();
    private Array<Player> players = null;
    private Rectangle playField = new Rectangle();
    private Array<Race> races;

    public World(WorldScreen screen) {
        this.screen = screen;
    }

    void addStar(Entity star) {
    }

    public void dispose() {
    }

    public Player findPlayerByName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return Player.nullPlayer;
    }

    public Entity findEntityById(String id) {
        ImmutableArray<Entity> entities = screen.getEntityEngine().getEntitiesFor(Family.getFor(IdComponent.class));
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            if (Mappers.id.get(entity).id.equals(id)) {
                return entity;
            }
        }
        return null;
    }

    void setPlayField(Rectangle rectangle) {
        this.playField.set(rectangle);
    }

    public Rectangle getPlayField() {
        return playField;
    }

    public Array<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Array<Player> players) {
        this.players = players;
    }

    public float getUnitsPerParsec() {
        return 100.0f;
    }

    public Array<Race> getRaces() {
        return races;
    }

    public void setRaces(Array<Race> races) {
        this.races = races;
    }
}
