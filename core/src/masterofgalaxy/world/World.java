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

    public Player findPlayerByName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return Player.nullPlayer;
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
}
