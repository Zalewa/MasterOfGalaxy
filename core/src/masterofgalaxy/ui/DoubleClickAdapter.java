package masterofgalaxy.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public abstract class DoubleClickAdapter extends ClickListener {

    private float prevX;
    private float prevY;

    public DoubleClickAdapter() {
        super();
    }

    public DoubleClickAdapter(int button) {
        super(button);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        if (getTapCount() % 2 == 0) {
            if (inTapSquare(prevX, prevY)) {
                doubleClicked(event, x, y);
            }
        }
        prevX = x;
        prevY = y;
    }

    protected abstract void doubleClicked(InputEvent event, float x, float y);
}
