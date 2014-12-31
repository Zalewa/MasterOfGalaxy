package masterofgalaxy.world.ui;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.UiSkin;
import masterofgalaxy.mainmenu.MainMenu;
import masterofgalaxy.ui.ConsumeTouchListener;
import masterofgalaxy.ui.Ui;

public class GlobalUi {
    private MogGame game;
    private Stage stage;
    private MainMenu mainMenu;

    public GlobalUi(MogGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        stage.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (mainMenu.isVisible()) {
                    if (event instanceof InputEvent) {
                        InputEvent inputEvent = (InputEvent) event;
                        if (inputEvent.getType() == InputEvent.Type.keyDown && inputEvent.getKeyCode() == Input.Keys.ESCAPE) {
                            setMainMenuVisible(false);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        setupMainMenuWindow();
    }

    public void centerMainMenu() {
        mainMenu.setPosition(
                (stage.getWidth() - mainMenu.getWidth()) * 0.5f,
                (stage.getHeight() - mainMenu.getHeight()) * 0.5f);
    }

    public void render(float delta) {
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();;
    }

    public void setMainMenuVisible(boolean visible) {
        mainMenu.setVisible(visible);
    }

    public boolean isMainMenuVisible() {
        return mainMenu.isVisible();
    }

    public void updateScreenSize(int width, int height) {
        stage.getViewport().update(width, height, true);
        //centerMainMenu();
    }

    public Stage getStage() {
        return stage;
    }

    private void setupMainMenuWindow() {
        mainMenu = new MainMenu(game, game.getAssetManager().get(UiSkin.skin));
        mainMenu.setColor(1.0f, 1.0f, 1.0f, 0.9f);
        mainMenu.addListener(new ConsumeTouchListener());

        mainMenu.cancelRequested.add(new Listener<Object>() {
            @Override
            public void receive(Signal<Object> signal, Object object) {
                setMainMenuVisible(false);
            }
        });

        stage.addActor(mainMenu);

        //centerMainMenu();
        mainMenu.setVisible(false);
    }
}
