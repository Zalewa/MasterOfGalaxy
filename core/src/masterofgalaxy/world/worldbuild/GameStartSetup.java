package masterofgalaxy.world.worldbuild;

import masterofgalaxy.MogGame;
import masterofgalaxy.gamestate.PlayerColor;
import masterofgalaxy.gamestate.Race;

import com.badlogic.gdx.math.MathUtils;

public class GameStartSetup {
    public final static int SMALL_WORLD = 10;
    public final static int MEDIUM_WORLD = 15;
    public final static int LARGE_WORLD = 20;

    public final static int MIN_RIVALS = 1;
    public final static int MAX_RIVALS = 5;

    private String playerName;
    private PlayerColor playerColor;
    private Race playerRace;
    private int numRivals;
    private int worldSize;

    public PlayerSetup getPlayerSetup() {
        PlayerSetup setup = new PlayerSetup();
        setup.setName(playerName);
        setup.setColor(playerColor);
        setup.setRace(playerRace);
        return setup;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public Race getPlayerRace() {
        return playerRace;
    }

    public void setPlayerRace(Race playerRace) {
        this.playerRace = playerRace;
    }

    public int getNumRivals() {
        return numRivals;
    }

    public void setNumRivals(int numRivals) {
        this.numRivals = numRivals;
    }

    public int getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }

    public static GameStartSetup randomize(MogGame game) {
        GameStartSetup setup = new GameStartSetup();
        setup.setPlayerName("Bob");
        setup.setNumRivals(MathUtils.random(MIN_RIVALS, MAX_RIVALS));
        setup.setWorldSize(MathUtils.random(SMALL_WORLD, LARGE_WORLD));
        setup.setPlayerColor(game.getActorAssets().playerColors.colors.random());
        setup.setPlayerRace(game.getActorAssets().races.races.random());
        return setup;
    }
}
