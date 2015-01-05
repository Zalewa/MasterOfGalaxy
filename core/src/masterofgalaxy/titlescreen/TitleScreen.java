package masterofgalaxy.titlescreen;

import com.badlogic.gdx.ScreenAdapter;
import masterofgalaxy.MogGame;
import masterofgalaxy.world.ui.GlobalUi;

public class TitleScreen extends ScreenAdapter {
    MogGame game;
    GlobalUi ui;

    public TitleScreen(MogGame game) {
        this.game = game;

        ui = new GlobalUi(game);
        ui.setMainMenuCancelable(false);
        ui.setCanGoToTitleScreen(false);
        ui.packMainMenu();
    }

    @Override
    public void show() {
        ui.setCanResumeGame(game.isGameResumable());
        ui.setCanSaveGame(game.isGameSavable());
        ui.packMainMenu();
        game.getInputMultiplexer().addProcessor(ui.getStage());
    }

    @Override
    public void hide() {
        game.getInputMultiplexer().removeProcessor(ui.getStage());
    }

    @Override
    public void render(float delta) {
        ui.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        ui.updateScreenSize(width, height);
    }

    @Override
    public void dispose() {
        ui.dispose();
    }

    public void reset() {
        ui.setMainMenuVisible(true);
    }
}
