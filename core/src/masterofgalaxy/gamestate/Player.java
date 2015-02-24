package masterofgalaxy.gamestate;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.graphics.Color;

public class Player {
    public static final Player nullPlayer;

    static {
        nullPlayer = new Player();
        nullPlayer.setName("");
        nullPlayer.setPlayerColor(PlayerColor.nullColor);
        nullPlayer.valid = false;
    }

    private final PlayerState state = new PlayerState();
    private Race race;
    private boolean valid = true;

    public Player() {
    }

    public Player(PlayerState state) {
        this.state.set(state);
    }

    public Signal<Color> colorChanged = new Signal<Color>();

    public Color getColor() {
        return state.getPlayerColor().getColor();
    }

    public PlayerColor getPlayerColor() {
        return state.getPlayerColor();
    }

    public void setPlayerColor(PlayerColor color) {
        state.setPlayerColor(color);
        colorChanged.dispatch(state.getPlayerColor().getColor());
    }

    public String getName() {
        return state.getName();
    }

    public void setName(String name) {
        state.setName(name);
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
        state.setRaceName(race.getName());
    }

    public boolean isValid() {
        return valid;
    }

    public PlayerState getState() {
        return state;
    }

    public float getProductionCostPerPopulationGrowth() {
        return 100.0f;
    }

    public float getFactoryPopulationCapacity() {
        return 2.0f;
    }

    public float getProductionCostPerFactory() {
        return 10.0f;
    }

    public float getProductionCostPerDefenseBase() {
        return 50.0f;
    }

    public float getProductionPerJoblessPopulation() {
        return 1.0f * getRace().getProductionRate();
    }

    public float getProductionPerMannedFactory() {
        return 5.0f * getRace().getProductionRate();
    }
}
