package masterofgalaxy.mainmenu.options;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.I18N;
import masterofgalaxy.config.VideoDisplayMode;
import masterofgalaxy.ui.ConsumeTouchListener;

public class OptionsMenu extends Window {
    private Skin skin;
    private VideoModeOptions videoModeOptions;
    private TextButton applyButton;
    private TextButton closeButton;
    private Table buttonRow = new Table();
    private Button xButton;
    private Table mainLayout;
    private MogGame game;

    public OptionsMenu(MogGame game, Skin skin) {
        super("options", skin);
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

        setupVideoModeWidget();

        setupButtonRow();
        setupApplyButton();
        setupCloseButton();

        applyTranslation();
        pack();
    }

    private void setupVideoModeWidget() {
        videoModeOptions = new VideoModeOptions(skin);
        mainLayout.add(videoModeOptions).expand().fillX().top();
        mainLayout.row();
    }

    private void setupButtonRow() {
        mainLayout.add(new Widget()).minHeight(30.0f);
        mainLayout.row();
        buttonRow = new Table(skin);
        buttonRow.defaults().space(10.0f).bottom();
        mainLayout.add(buttonRow);
        mainLayout.row();
    }

    private void setupCloseButton() {
        closeButton = new TextButton("close", skin);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });
        buttonRow.add(closeButton);
    }

    private void setupApplyButton() {
        applyButton = new TextButton("apply", skin);
        applyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                apply();
            }
        });
        buttonRow.add(applyButton);
    }

    private void setupXButton() {
        xButton = new TextButton("X", skin);
        xButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });

        getButtonTable().add(xButton).height(getPadTop());
    }

    private void apply() {
        videoModeOptions.apply();
    }

    private void close() {
        clear();
        remove();
    }

    private void applyTranslation() {
        setTitle(I18N.i18n.format("$options"));
        applyButton.setText(I18N.i18n.format("$apply"));
        closeButton.setText(I18N.i18n.format("$close"));
    }
}
