package masterofgalaxy.assets.i18n;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import masterofgalaxy.exceptions.DuplicateException;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.JsonReader;

public class I18N {
    private static Map<String, L10NSource> namedBundles = new LinkedHashMap<String, L10NSource>();
    private static Locale locale;
    private static LocalizationEntry currentLocalization;
    private static Array<LocalizationEntry> localizations = new Array<LocalizationEntry>();

    public static I18NBundle i18n;
    public static Signal<Object> localeChanged = new Signal<Object>();

    private static List<LocalizableRegistry> localizables
        = new LinkedList<LocalizableRegistry>();

    public static void addLocalizable(Localizable localizable) {
        if (findLocalizable(localizable) != null) {
            throw new DuplicateException("tried to register localizable twice");
        }
        LocalizationChangedListener listener = new LocalizationChangedListener(localizable);
        localizables.add(new LocalizableRegistry(localizable, listener));
        localeChanged.add(listener);
    }

    public static void removeLocalizable(Localizable localizable) {
        localeChanged.remove(findLocalizable(localizable).listener);
        localizables.remove(findLocalizable(localizable));
    }

    private static LocalizableRegistry findLocalizable(Localizable localizable) {
        for (LocalizableRegistry registry : localizables) {
            if (registry.localizable == localizable) {
                return registry;
            }
        }
        return null;
    }

    public static void loadLocalizations(FileHandle file) {
        JsonReader reader = new JsonReader();
        localizations.addAll(LocalizationEntry.parseJson(reader.parse(file)));
    }

    public static void load(LocalizationEntry localization) {
        currentLocalization = localization;
        I18N.locale = localization.getLocale();
        System.out.println("Loading locale: " + locale);
        Locale.setDefault(locale);
        i18n = I18NBundle.createBundle(Gdx.files.internal("i18n/i18n"), locale);

        Signal<Object> suppressed = localeChanged;
        localeChanged = new Signal<Object>();
        reloadBundles();
        localeChanged = suppressed;
        localeChanged.dispatch(null);
    }

    public static void loadNamed(String name, FileHandle fileHandle) {
        I18NBundle bundle = I18NBundle.createBundle(fileHandle, locale);
        namedBundles.put(name, new L10NSource(bundle, fileHandle));
        localeChanged.dispatch(null);
    }

    public static String resolve(String key, Object... args) {
        try {
            return i18n.format(key, args);
        } catch (MissingResourceException e) {
            // Do nothing, we will return the key.
        }
        return key;
    }

    public static String resolveNamed(String bundleName, String key, Object... args) {
        I18NBundle bundle = getNamedBundle(bundleName);
        if (bundle != null) {
            try {
                return bundle.format(key, args);
            } catch (MissingResourceException e) {
                // Do nothing, we will return the key.
            }
        }
        return key;
    }

    public static String formatFloat(float f, String formatter) {
        return MessageFormat.format(formatter, f);
    }

    private static void reloadBundles() {
        Map<String, L10NSource> bundles = namedBundles;
        namedBundles = new LinkedHashMap<String, L10NSource>();
        for (String name : bundles.keySet()) {
            loadNamed(name, bundles.get(name).getSource());
        }
    }

    private static I18NBundle getNamedBundle(String bundleName) {
        L10NSource source = namedBundles.get(bundleName);
        if (source != null) {
            return source.getBundle();
        }
        return null;
    }

    public static LocalizationEntry getBestFittingLocalization(Locale locale) {
        LocalizationEntry entry = LocalizationEntry.pickBestFitting(localizations, locale);
        if (entry == null) {
            entry = LocalizationEntry.pickDefault(localizations);
        }
        return entry;
    }

    public static Array<LocalizationEntry> getLocalizations() {
        return localizations;
    }

    public static Locale getLocale() {
        return locale;
    }

    public static LocalizationEntry getCurrentLocalization() {
        return currentLocalization;
    }

    private static class LocalizableRegistry {
        public Localizable localizable;
        public LocalizationChangedListener listener;

        public LocalizableRegistry(Localizable localizable,
                LocalizationChangedListener listener) {
            this.localizable = localizable;
            this.listener = listener;
        }

    }
}
