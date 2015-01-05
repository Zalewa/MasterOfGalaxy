package masterofgalaxy.gamestate;

import com.badlogic.gdx.graphics.Color;

public class PlayerState {
    String name;
    Color color;

    public PlayerState() {
        this.color = Color.WHITE.cpy();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set(PlayerState other) {
        setName(other.getName());
        setColor(other.getColor());
    }
}
