package masterofgalaxy.mainmenu.options;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.LocalizationEntry;
import masterofgalaxy.config.GeneralConfig;

import java.util.Locale;

public class LanguageOptions extends Table {
    private Skin skin;
    private Label label;
    private SelectBox<LocalizationEntry> languageSelectBox;

    public LanguageOptions(Skin skin) {
        super(skin);
        this.skin = skin;
        I18N.localeChanged.add(new Listener<Object>() {
            @Override
            public void receive(Signal<Object> signal, Object object) {
                applyTranslation();
            }
        });

        defaults().left().space(10.0f);

        label = new Label("language", skin);
        add(label);

        languageSelectBox = new SelectBox<LocalizationEntry>(skin);
        add(languageSelectBox);

        loadLocalizations();
        setCurrentLocalization();

        applyTranslation();
    }

    public void apply() {
        applyLanguage();
        saveConfig();
    }

    private void applyLanguage() {
        I18N.load(getSelectedEntry());
    }

    private void saveConfig() {
        GeneralConfig config = new GeneralConfig();
        config.setLocale(I18N.getLocale());
    }

    private void loadLocalizations() {
        languageSelectBox.setItems(I18N.getLocalizations());
    }

    private void setCurrentLocalization() {
        Locale locale = I18N.getLocale();
        LocalizationEntry entry = LocalizationEntry.pickBestFitting(languageSelectBox.getItems(), locale);
        if (entry == null) {
            entry = LocalizationEntry.pickDefault(languageSelectBox.getItems());
        }
        languageSelectBox.setSelected(entry);
    }

    private LocalizationEntry getSelectedEntry() {
        return languageSelectBox.getSelected();
    }

    private void applyTranslation() {
        label.setText(I18N.i18n.format("$language_colon"));
    }
}
