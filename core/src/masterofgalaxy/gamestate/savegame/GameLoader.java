package masterofgalaxy.gamestate.savegame;

import com.badlogic.gdx.utils.Json;

import java.io.FileReader;

public class GameLoader {
    private FileReader reader;

    public GameLoader(FileReader reader) {
        this.reader = reader;
    }

    public GameState load() {
        Json json = new Json();
        return json.fromJson(GameState.class, reader);
    }
}
