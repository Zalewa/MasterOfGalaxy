package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Pool;

public class TextRenderComponent extends Component implements Pool.Poolable {
    public BitmapFont font;
    public String text;
    public Color tint = Color.WHITE.cpy();

    private Signal<String> listenedTextChangedSignal;
    private Listener<String> textChangedListener = new Listener<String>() {
        @Override
        public void receive(Signal<String> signal, String newText) {
            text = newText;
        }
    };

    private Signal<Color> listenedColorChangedSignal;
    private Listener<Color> colorChangedListener = new Listener<Color>() {
        @Override
        public void receive(Signal<Color> signal, Color newColor) {
            tint.set(newColor);
        }
    };

    @Override
    public void reset() {
        font = null;
        text = "";
        tint.set(Color.WHITE);
        resetTextChangeListener();
        resetColorChangeListener();
    }

    public void registerTextChangedSignal(Signal<String> signal) {
        resetTextChangeListener();
        listenedTextChangedSignal = signal;
        listenedTextChangedSignal.add(textChangedListener);
    }

    private void resetTextChangeListener() {
        if (listenedTextChangedSignal != null) {
            listenedTextChangedSignal.remove(textChangedListener);
        }
    }

    public void registerColorChangedSignal(Signal<Color> signal) {
        resetColorChangeListener();
        listenedColorChangedSignal = signal;
        listenedColorChangedSignal.add(colorChangedListener);
    }

    private void resetColorChangeListener() {
        if (listenedColorChangedSignal != null) {
            listenedColorChangedSignal.remove(colorChangedListener);
        }
    }
}
