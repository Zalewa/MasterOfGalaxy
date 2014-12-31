package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.gamestate.Player;

public class World {
    private WorldScreen screen;
    private Array<Entity> entities = new Array<Entity>();
    private Array<Player> players = null;
    private Rectangle playField = new Rectangle();

    public World(WorldScreen screen) {
        this.screen = screen;
    }

    void addStar(Entity star) {
    }

    public void dispose() {
    }

    void setPlayField(Rectangle rectangle) {
        this.playField = rectangle;
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
}
