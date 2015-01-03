package masterofgalaxy.world.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.UiSkin;
import masterofgalaxy.mainmenu.MainMenu;
import masterofgalaxy.ui.Ui;

public class GlobalUi {
    private MogGame game;
    private Stage stage;
    private MainMenu mainMenu;

    public GlobalUi(MogGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
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
        stage.draw();
    }

    public void setMainMenuVisible(boolean visible) {
        if (visible) {
            stage.addActor(mainMenu);
            mainMenu.setVisible(true);
            Ui.centerWithinStage(mainMenu);
        } else {
            mainMenu.remove();
        }
    }

    public boolean isMainMenuVisible() {
        return mainMenu.getParent() == stage.getRoot() && mainMenu.isVisible();
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
        mainMenu = new MainMenu(game, game.getAssetManager().get(UiSkin.skin));
        mainMenu.setColor(1.0f, 1.0f, 1.0f, 0.9f);
    }
}
