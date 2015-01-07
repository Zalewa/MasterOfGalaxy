package masterofgalaxy.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public abstract class EscapeKeyAdapter extends InputListener {
    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            boolean escaped = escape();
            return escaped;
        }
        return false;
    }

    protected abstract boolean escape();
}
