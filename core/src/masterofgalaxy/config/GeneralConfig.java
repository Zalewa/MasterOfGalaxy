package masterofgalaxy.config;

import com.badlogic.gdx.Preferences;

import java.util.Locale;

public class GeneralConfig {
    private final Preferences prefs;

    public GeneralConfig() {
        this.prefs = PrefsProvider.main();
    }

    public GeneralConfig(Preferences prefs) {
        this.prefs = prefs;
    }

    public Locale getLocale() {
        if (!prefs.contains("Locale/language")) {
            return Locale.getDefault();
        } else {
            return new Locale(prefs.getString("Locale/language"), prefs.getString("Locale/country"), prefs.getString("Locale/variant"));
        }
    }

    public void setLocale(Locale locale) {
        prefs.putString("Locale/language", locale.getLanguage());
        prefs.putString("Locale/country", locale.getCountry());
        prefs.putString("Locale/variant", locale.getVariant());
    }

    public void flush() {
        prefs.flush();
    }
}
