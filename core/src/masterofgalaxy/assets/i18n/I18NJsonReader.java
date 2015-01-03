package masterofgalaxy.assets.i18n;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;

public class I18NJsonReader {
    public static String getBundleName(JsonValue json) {
        if (json != null) {
            return json.getString("bundle");
        }
        return null;
    }

    public static String loadNamedBundle(JsonValue root, Files.FileType fileType) {
        String bundle = I18NJsonReader.getBundleName(root.get("i18n"));
        if (bundle != null) {
            I18N.loadNamed(bundle, Gdx.files.getFileHandle(bundle, fileType));
        }
        return bundle;
    }
}
