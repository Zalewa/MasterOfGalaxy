package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class FleetComponent extends Component implements Pool.Poolable {
    @Override
    public void reset() {

    }

    public float getSpeedInParsecsPerTurn() {
        return 1.0f;
    }
}
