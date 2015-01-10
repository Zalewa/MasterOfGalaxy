package masterofgalaxy.gamestate;

public class PlayerState {
    String name;
    PlayerColor playerColor = PlayerColor.nullColor;
    private String raceName;

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

    void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getRaceName() {
        return raceName;
    }

    public void set(PlayerState other) {
        setName(other.getName());
        setPlayerColor(other.getPlayerColor());
        setRaceName(other.getRaceName());
    }
}
