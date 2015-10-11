package masterofgalaxy.mainmenu.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationChangedListener;
import masterofgalaxy.ui.EscapeKeyAdapter;

public class OptionsMenu extends Window implements Localizable {
    private VideoModeOptions videoModeOptions;
    private LanguageOptions languageOptions;
    private TextButton applyButton;
    private TextButton closeButton;
    private Table buttonRow = new Table();
    private Button xButton;
    private Table mainLayout;
    private LocalizationChangedListener translationListener = new LocalizationChangedListener(this);

    public OptionsMenu(MogGame game, Skin skin) {
        super("options", skin);
        I18N.localeChanged.add(translationListener);
        setModal(true);
        addListener(new EscapeKeyAdapter() {
            @Override
            protected boolean escape() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        close();
                    }
                });
                return false;
            }
        });

        mainLayout = new Table(skin);
        mainLayout.pad(10.0f);
        mainLayout.defaults().space(5.0f);
        add(mainLayout).expand().fill();

        setupXButton();

        setupVideoModeWidget();
        setupLanguageWidget();

        setupButtonRow();
        setupApplyButton();
        setupCloseButton();

        applyTranslation();
        pack();
    }

    private void setupVideoModeWidget() {
        videoModeOptions = new VideoModeOptions(getSkin());
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
                close();
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

    private void setupXButton() {
        xButton = new TextButton("X", getSkin());
        xButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });

        getTitleTable().add(xButton).height(getPadTop());
    }

    private void apply() {
        videoModeOptions.apply();
        languageOptions.apply();
    }

    private void close() {
        I18N.localeChanged.remove(translationListener);
        clear();
        remove();
    }

    @Override
    public void applyTranslation() {
        getTitleLabel().setText(I18N.i18n.format("$options"));
        applyButton.setText(I18N.i18n.format("$apply"));
        closeButton.setText(I18N.i18n.format("$close"));
    }
}
