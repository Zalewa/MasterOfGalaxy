package masterofgalaxy.mainmenu;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.I18N;
import masterofgalaxy.mainmenu.options.OptionsMenu;
import masterofgalaxy.ui.ConsumeTouchListener;

public class MainMenu extends Window {
    private Skin skin;
    private TextButton optionsButton;
    private TextButton exitButton;
    private TextButton cancelButton;
    private Button xButton;
    private Table mainLayout;
    private MogGame game;

    public Signal<Object> cancelRequested = new Signal<Object>();

    public MainMenu(MogGame game, Skin skin) {
        super("title", skin);
        I18N.localeChanged.add(new Listener<Object>() {
            @Override
            public void receive(Signal<Object> signal, Object object) {
                applyTranslation();
            }
        });
        this.game = game;
        this.skin = skin;
        addListener(new ConsumeTouchListener());

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
                cancelRequested.dispatch(null);
            }
        });

        getButtonTable().add(xButton).height(getPadTop());
    }

    private void setupOptionsButton() {
        optionsButton = new TextButton("Options", skin);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getParent().addActor(new OptionsMenu(game, skin));
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
                cancelRequested.dispatch(null);
            }
        });

        mainLayout.add(cancelButton).bottom();
        mainLayout.row();
    }

    private void applyTranslation() {
        setTitle(I18N.i18n.format("$mainMenu"));
        exitButton.setText(I18N.i18n.format("$exitGame"));
        cancelButton.setText(I18N.i18n.format("$cancel"));
    }
}
