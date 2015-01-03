package masterofgalaxy.assets.i18n;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Locale;

public class LocalizationEntry {
    private String name;
    private String country = "";
    private String language = "";
    private String variant = "";

    public static Array<LocalizationEntry> parseJson(JsonValue jsonRoot) {
        Array<LocalizationEntry> result = new Array<LocalizationEntry>();
        JsonValue l10n = jsonRoot.get("l10n");
        for (int i = 0; i < l10n.size; ++i) {
            JsonValue localizationDef = l10n.get(i);

            LocalizationEntry entry = new LocalizationEntry();
            entry.setName(localizationDef.getString("name"));
            entry.setCountry(localizationDef.getString("country", ""));
            entry.setLanguage(localizationDef.getString("language", ""));
            entry.setVariant(localizationDef.getString("variant", ""));
            result.add(entry);
        }
        return result;
    }

    public int similarityScore(Locale locale) {
        int score = 0;
        if (locale.getLanguage().compareTo(language) == 0) {
            ++score;
            if (locale.getCountry().compareTo(country) == 0) {
                ++score;
                if (locale.getVariant().compareTo(variant) == 0) {
                    ++score;
                }
            }
        }
        return score;
    }

    public boolean isDefault() {
        return country.equals("") && language.equals("") && variant.equals("");
    }

    @Override
    public String toString() {
        return name;
    }

    public Locale getLocale() {
        return new Locale(language, country, variant);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    private static String emptyStrIfNull(String s) {
        return s != null ? s : "";
    }
}
