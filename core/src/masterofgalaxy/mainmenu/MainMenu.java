package masterofgalaxy.mainmenu;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationChangedListener;
import masterofgalaxy.mainmenu.options.OptionsMenu;
import masterofgalaxy.ui.ActorRemoveEscapeKeyAdapter;
import masterofgalaxy.ui.Ui;

public class MainMenu extends Window implements Localizable {
    private Skin skin;
    private TextButton optionsButton;
    private TextButton exitButton;
    private TextButton cancelButton;
    private Button xButton;
    private Table mainLayout;
    private MogGame game;

    public MainMenu(MogGame game, Skin skin) {
        super("title", skin);
        I18N.localeChanged.add(new LocalizationChangedListener(this));
        this.game = game;
        this.skin = skin;
        setModal(true);
        addListener(new ActorRemoveEscapeKeyAdapter(this));

        mainLayout = new Table(skin);
        mainLayout.pad(10.0f);
        mainLayout.defaults().space(5.0f);
        add(mainLayout).expand().fill();

        setupXButton();
        setupOptionsButton();
        setupExitButton();
        setupCancelButton();

        applyTranslation();
    }

    private void setupXButton() {
        xButton = new TextButton("X", skin);
        xButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                remove();
            }
        });

        getButtonTable().add(xButton).height(getPadTop());
    }

    private void setupOptionsButton() {
        optionsButton = new TextButton("Options", skin);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                OptionsMenu menu = new OptionsMenu(game, skin);
                getParent().addActor(menu);
                getStage().setKeyboardFocus(menu);
                Ui.centerWithinStage(menu);
            }
        });

        mainLayout.add(optionsButton).fillX();
        mainLayout.row();
    }

    private void setupExitButton() {
        exitButton = new TextButton("Exit Game", skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.exit();
            }
        });

        mainLayout.add(exitButton).expandY().uniformX().top();
        mainLayout.row();
    }

    private void setupCancelButton() {
        cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                remove();
            }
        });

        mainLayout.add(cancelButton).bottom();
        mainLayout.row();
    }

    @Override
    public void applyTranslation() {
        setTitle(I18N.i18n.format("$mainMenu"));
        optionsButton.setText(I18N.i18n.format("$options"));
        exitButton.setText(I18N.i18n.format("$exitGame"));
        cancelButton.setText(I18N.i18n.format("$cancel"));
    }
}
