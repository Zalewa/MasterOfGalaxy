package masterofgalaxy.assets.fonts;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.LinkedHashMap;
import java.util.Map;

public class FontDefParser {
    Map<String, FontDef> result;

    public Map<String, FontDef> parse(FileHandle file) {
        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(file);

        result = new LinkedHashMap<String, FontDef>();
        parseFonts(root.get("fonts"));
        return result;
    }

    private void parseFonts(JsonValue fonts) {
        for (int i = 0; i < fonts.size; ++i) {
            FontDef def = parseFontDef(fonts.get(i));

            String name = fonts.get(i).name();
            def.setName(name);
            result.put(name, def);
        }
    }

    private FontDef parseFontDef(JsonValue jsonValue) {
        FontDef result = new FontDef();
        result.setFile(jsonValue.getString("file"));
        result.setSize(jsonValue.getInt("size"));
        result.setKerning(jsonValue.getBoolean("kerning", true));
        result.setGenMipMaps(jsonValue.getBoolean("getMipMaps", true));
        result.setMinFilter(parseTextureFilter(jsonValue.getString("minFilter", Texture.TextureFilter.Linear.name())));
        result.setMagFilter(parseTextureFilter(jsonValue.getString("magFilter", Texture.TextureFilter.Linear.name())));
        return result;
    }

    private Texture.TextureFilter parseTextureFilter(String str) {
        return Texture.TextureFilter.valueOf(str);
    }
}
