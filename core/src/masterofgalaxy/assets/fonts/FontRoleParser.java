package masterofgalaxy.assets.fonts;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.LinkedHashMap;
import java.util.Map;

public class FontRoleParser {
    public Map<String, String> parse(FileHandle file) {
        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(file);
        JsonValue fontroles = root.get("fontroles");

        Map<String, String> result = new LinkedHashMap<String, String>();
        for (int i = 0; i < fontroles.size; ++i) {
            JsonValue role = fontroles.get(i);
            result.put(role.name, fontroles.getString(role.name));
        }
        return result;
    }
}
