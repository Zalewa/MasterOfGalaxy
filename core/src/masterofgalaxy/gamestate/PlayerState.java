package masterofgalaxy.gamestate;

public class PlayerState {
    String name;
    PlayerColor playerColor = PlayerColor.nullColor;

    public PlayerState() {

    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerColor color) {
        this.playerColor = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set(PlayerState other) {
        setName(other.getName());
        setPlayerColor(other.getPlayerColor());
    }
}
