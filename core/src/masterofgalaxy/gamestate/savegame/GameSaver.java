package masterofgalaxy.gamestate.savegame;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.exceptions.SavedGameException;
import masterofgalaxy.world.WorldScreen;

import java.io.IOException;
import java.io.Writer;

public class GameSaver {
    private MogGame game;
    private Writer writer;
    private Json json;

    public GameSaver(MogGame game, Writer writer) {
        this.game = game;
        this.writer = writer;

        json = new Json(JsonWriter.OutputType.json);
    }

    public void save() throws SavedGameException {
        try {
            saveWrapped();
        } catch (Exception e) {
            throw new SavedGameException(I18N.resolve("$gameCannotBeSaved", e.getMessage()), e);
        }
    }

    private void saveWrapped() throws IOException {
        WorldScreen worldScreen = game.getWorldScreen();

        GameState state = new GameState();
        state.setWorldState(worldScreen.buildWorldState());
        writer.write(json.prettyPrint(state));
    }
}
