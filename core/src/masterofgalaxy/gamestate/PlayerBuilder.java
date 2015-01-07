package masterofgalaxy.gamestate;

import com.badlogic.gdx.utils.Array;
import masterofgalaxy.MogGame;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PlayerBuilder {
    public static Array<Player> fromStates(Array<PlayerState> states) {
        Array<Player> result = new Array<Player>();
        for (PlayerState state : states) {
            result.add(new Player(state));
        }
        return result;
    }

    private Random random;
    private MogGame game;

    public PlayerBuilder(MogGame game, long seed) {
        this.game = game;
        random = new Random(seed);
    }

    public Array<Player> randomizePlayers(int count) {
        List<PlayerColor> colors = new LinkedList<PlayerColor>();
        for (PlayerColor color : game.getActorAssets().playerColors.colors) {
            colors.add(color);
        }
        Collections.shuffle(colors, random);

        if (colors.size() < count) {
            throw new IllegalArgumentException("player count cannot be greater than available colors count");
        }

        Array<Player> result = new Array<Player>();
        for (int i = 0; i < count; ++i) {
            Player player = new Player();
            player.setName("Player-" + i);
            player.setPlayerColor(colors.get(i));
            result.add(player);
        }

        return result;
    }
}
