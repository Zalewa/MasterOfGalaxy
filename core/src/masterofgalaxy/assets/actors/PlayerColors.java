package masterofgalaxy.assets.actors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import masterofgalaxy.assets.i18n.I18NJsonReader;
import masterofgalaxy.exceptions.InvalidInputTypeException;
import masterofgalaxy.gamestate.PlayerColor;

public class PlayerColors {
    public Array<PlayerColor> colors = new Array<PlayerColor>();

    public PlayerColor findColor(Color color) {
        for (PlayerColor playerColor : colors) {
            if (playerColor.getColor().equals(color)) {
                return playerColor;
            }
        }
        return PlayerColor.nullColor;
    }

    public void load(FileHandle file) {
        JsonReader reader = new JsonReader();
        JsonValue jsonRoot = reader.parse(file);
        String localizationBundleName = I18NJsonReader.loadNamedBundle(jsonRoot, file.type());
        JsonValue jsonColors = jsonRoot.get("colors");
        if (jsonColors == null || !jsonColors.isArray()) {
            throw new InvalidInputTypeException(file.path() + " is not a colors file");
        }
        for (int i = 0; i < jsonColors.size; ++i) {
            JsonValue jsonColor = jsonColors.get(i);

            PlayerColor color = new PlayerColor();
            color.setName(jsonColor.getString("name"));
            color.setLocalizationBundleName(localizationBundleName);
            color.setColor(Color.valueOf(jsonColor.getString("color")));

            colors.add(color);
        }
    }
}
