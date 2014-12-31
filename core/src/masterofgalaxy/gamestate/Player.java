package masterofgalaxy.gamestate;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.graphics.Color;

public class Player {
    public static final Player nullPlayer;

    static {
        nullPlayer = new Player();
        nullPlayer.setName("null");
        nullPlayer.setColor(Color.DARK_GRAY);
        nullPlayer.valid = false;
    }

    private boolean valid = true;
    private String name;
    private Color color = Color.WHITE.cpy();

    public Signal<Color> colorChanged = new Signal<Color>();

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color.set(color);
        colorChanged.dispatch(this.color);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isValid() {
        return valid;
    }
}
