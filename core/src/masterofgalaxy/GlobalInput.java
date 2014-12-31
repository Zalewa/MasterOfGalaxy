package masterofgalaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class GlobalInput extends InputAdapter {
    private MogGame game;

    public GlobalInput(MogGame game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ENTER && Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
            game.toggleFullscreen();
            return true;
        }
        return false;
    }
}
