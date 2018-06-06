package masterofgalaxy.mainmenu.options;

import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.ui.WindowExtender;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class OptionsMenu extends Window implements Localizable {
    public WindowExtender ex;
    private VideoModeOptions videoModeOptions;
    private LanguageOptions languageOptions;
    private TextButton applyButton;
    private TextButton closeButton;
    private Table buttonRow = new Table();
    private Table mainLayout;

    public OptionsMenu(MogGame game, Skin skin) {
        super("options", skin);
        setModal(true);
        ex = new WindowExtender(this);
        ex.setDestroyOnClose(true);

        mainLayout = new Table(skin);
        mainLayout.pad(10.0f);
        mainLayout.defaults().space(5.0f);
        add(mainLayout).expand().fill();

        setupVideoModeWidget(game);
        setupLanguageWidget();

        setupButtonRow();
        setupApplyButton();
        setupCloseButton();

        applyTranslation();
        pack();
    }

    private void setupVideoModeWidget(MogGame game) {
        videoModeOptions = new VideoModeOptions(game, getSkin());
        mainLayout.add(videoModeOptions).expand().fillX().top();
        mainLayout.row();
    }

    private void setupLanguageWidget() {
        languageOptions = new LanguageOptions(getSkin());
        mainLayout.add(languageOptions).expand().fillX().top();
        mainLayout.row();
    }

    private void setupButtonRow() {
        mainLayout.add(new Widget()).minHeight(30.0f);
        mainLayout.row();
        buttonRow = new Table(getSkin());
        buttonRow.defaults().space(10.0f).bottom();
        mainLayout.add(buttonRow);
        mainLayout.row();
    }

    private void setupCloseButton() {
        closeButton = new TextButton("close", getSkin());
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                remove();
            }
        });
        buttonRow.add(closeButton);
    }

    private void setupApplyButton() {
        applyButton = new TextButton("apply", getSkin());
        applyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                apply();
            }
        });
        buttonRow.add(applyButton);
    }

    private void apply() {
        videoModeOptions.apply();
        languageOptions.apply();
    }

    @Override
    public void applyTranslation() {
        getTitleLabel().setText(I18N.i18n.format("$options"));
        applyButton.setText(I18N.i18n.format("$apply"));
        closeButton.setText(I18N.i18n.format("$close"));

        videoModeOptions.applyTranslation();
        languageOptions.applyTranslation();
    }
}
