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
        List<PlayerColor> colors = shufflePlayerColors();
        List<Race> races = shuffleRaces();

        if (colors.size() < count) {
            throw new IllegalArgumentException("player count cannot be greater than available colors count");
        }

        Array<Player> result = new Array<Player>();
        for (int i = 0; i < count; ++i) {
            Player player = new Player();
            player.setName("Player-" + i);
            player.setPlayerColor(colors.get(i));
            player.setRace(races.get(i % races.size()));
            result.add(player);
        }

        return result;
    }

    private List<Race> shuffleRaces() {
        List<Race> races = new LinkedList<Race>();
        for (Race race : game.getActorAssets().races.races) {
            races.add(race);
        }
        Collections.shuffle(races, random);
        return races;
    }

    private List<PlayerColor> shufflePlayerColors() {
        List<PlayerColor> colors = new LinkedList<PlayerColor>();
        for (PlayerColor color : game.getActorAssets().playerColors.colors) {
            colors.add(color);
        }
        Collections.shuffle(colors, random);
        return colors;
    }
}
