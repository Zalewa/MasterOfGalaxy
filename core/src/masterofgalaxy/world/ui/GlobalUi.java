package masterofgalaxy.world.ui;

import masterofgalaxy.MogGame;
import masterofgalaxy.assets.UiSkin;
import masterofgalaxy.mainmenu.MainMenu;
import masterofgalaxy.ui.Ui;
import masterofgalaxy.world.ui.research.ResearchWindow;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GlobalUi implements Disposable {
    private MogGame game;
    private Stage stage;
    private MainMenu mainMenu;
    private ResearchWindow researchWindow;
    private boolean debug = false;

    public GlobalUi(MogGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.D) {
                    debug = !debug;
                    stage.setDebugAll(debug);
                }
                return false;
            }
        });
        setupMainMenuWindow();
    }

    public void render(float delta) {
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    public void setMainMenuVisible(boolean visible) {
        if (visible) {
            stage.addActor(mainMenu);
            mainMenu.setVisible(true);
            packMainMenu();
            Ui.centerWithinStage(mainMenu);
        } else {
            mainMenu.remove();
        }
    }

    public boolean isMainMenuVisible() {
        return mainMenu.getParent() == stage.getRoot() && mainMenu.isVisible();
    }

    public void setMainMenuCancelable(boolean cancelable) {
        mainMenu.setCancelable(cancelable);
    }


    public void setCanGoToTitleScreen(boolean b) {
        mainMenu.setCanGoToTitleScreen(b);
    }

    public void setCanResumeGame(boolean gameResumable) {
        mainMenu.setCanResumeGame(gameResumable);
    }

    public void setCanSaveGame(boolean can) {
        mainMenu.setCanSaveGame(can);
    }

    public void updateScreenSize(int width, int height) {
        stage.getViewport().update(width, height, true);
        centerAllWindows();
    }

    private void centerAllWindows() {
        for (Actor actor : stage.getRoot().getChildren()) {
            if (actor instanceof Window) {
                Ui.centerWithinStage(actor);
            }
        }
    }

    public Stage getStage() {
        return stage;
    }

    private void setupMainMenuWindow() {
        mainMenu = new MainMenu(game, UiSkin.skin);
        mainMenu.setColor(1.0f, 1.0f, 1.0f, 0.9f);
    }

    @Override
    public void dispose() {
    }

    public void packMainMenu() {
        mainMenu.pack();
    }

    public void openResearchScreen() {
        researchWindow = new ResearchWindow(game, UiSkin.skin);
        researchWindow.addListener(new InputListener() {
            @Override
            public void exit(InputEvent event, float x, float y, int pointer,
                    Actor toActor) {
                researchWindow = null;
            }
        });
        stage.addActor(researchWindow);
        researchWindow.setVisible(true);
        Ui.centerWithinStage(researchWindow);
    }
}
