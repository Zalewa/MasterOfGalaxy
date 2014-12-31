package masterofgalaxy.assets;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class I18N {
    public static I18NBundle i18n;

    public static Signal<Object> localeChanged = new Signal<Object>();

    public static void load(Locale locale) {
        System.out.println("Loading locale: " + locale);
        i18n = I18NBundle.createBundle(Gdx.files.internal("i18n/i18n"), locale);
        localeChanged.dispatch(null);
    }
}
