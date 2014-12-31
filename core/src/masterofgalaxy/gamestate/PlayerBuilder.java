package masterofgalaxy.gamestate;

import com.badlogic.gdx.utils.Array;

import java.util.*;

public class PlayerBuilder {
    private Random random;

    public PlayerBuilder(long seed) {
        random = new Random(seed);
    }

    public Array<Player> randomizePlayers(int count) {
        List<PlayerColor> colors = Arrays.asList(PlayerColor.values());
        Collections.shuffle(colors, random);

        if (colors.size() < count) {
            throw new IllegalArgumentException("player count cannot be greater than available colors count");
        }

        Array<Player> result = new Array<Player>();
        for (int i = 0; i < count; ++i) {
            Player player = new Player();
            player.setName("Player-" + i);
            player.setColor(colors.get(i).getColor());
            result.add(player);
        }

        return result;
    }
}
