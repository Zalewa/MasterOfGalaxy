package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

public class BlinkComponent extends Component implements Pool.Poolable {
    public float magnitude = 0.1f;
    public float countdown = -1.0f;
    public float blinkMinInterval = 5.0f;
    public float blinkMaxInterval = 30.0f;
    public float blinkedMinInterval = 0.2f;
    public float blinkedMaxInterval = 0.21f;
    public boolean isBlinked = false;
    public Color colorBlinked = new Color();
    public Color colorUnblinked = new Color();

    @Override
    public void reset() {
        magnitude = 0.1f;
        countdown = -1.0f;
        isBlinked = false;
    }
}
