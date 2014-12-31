package masterofgalaxy.gamestate;

import com.badlogic.gdx.graphics.Color;

public enum PlayerColor {
    White(Color.WHITE), Red(Color.RED), Blue(Color.BLUE), Green(Color.GREEN), Teal(Color.TEAL), Purple(Color.PURPLE);

    private Color color;

    PlayerColor(Color color) {
        this.color = color.cpy();
    }

    public Color getColor() {
        return color;
    }
}
