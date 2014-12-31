package masterofgalaxy.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PrefsProvider {
    public static Preferences main() {
        return Gdx.app.getPreferences("MasterOfGalaxy");
    }
}
