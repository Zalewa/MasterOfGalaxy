package masterofgalaxy.mainmenu.options;

import java.util.Locale;

import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationEntry;
import masterofgalaxy.config.GeneralConfig;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LanguageOptions extends Table implements Localizable {
    private Label label;
    private SelectBox<LocalizationEntry> languageSelectBox;

    public LanguageOptions(Skin skin) {
        super(skin);

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

    @Override
    public void applyTranslation() {
        label.setText(I18N.i18n.format("$language_colon"));
    }
}
