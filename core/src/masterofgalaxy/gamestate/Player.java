package masterofgalaxy.gamestate;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.graphics.Color;

public class Player {
    public static final Player nullPlayer;

    static {
        nullPlayer = new Player();
        nullPlayer.setName("");
        nullPlayer.setColor(Color.DARK_GRAY);
        nullPlayer.valid = false;
    }

    private final PlayerState state = new PlayerState();
    private boolean valid = true;

    public Player() {
    }

    public Player(PlayerState state) {
        this.state.set(state);
    }

    public Signal<Color> colorChanged = new Signal<Color>();

    public Color getColor() {
        return state.getColor();
    }

    public void setColor(Color color) {
        state.setColor(color);
        colorChanged.dispatch(state.getColor());
    }

    public String getName() {
        return state.getName();
    }

    public void setName(String name) {
        state.setName(name);
    }

    public boolean isValid() {
        return valid;
    }

    public PlayerState getState() {
        return state;
    }
}
