package masterofgalaxy.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class ConsumeTouchAdapter implements EventListener {
    @Override
    public boolean handle(Event event) {
        if (shouldConsume(event)) {
            return true;
        }
        return false;
    }

    private boolean shouldConsume(Event event) {
        if (!(event instanceof InputEvent)) {
            return false;
        }
        InputEvent inputEvent = (InputEvent)event;
        if (inputEvent.getType() == InputEvent.Type.touchDown || inputEvent.getType() == InputEvent.Type.touchUp) {
            return true;
        }
        return false;
    }
}
