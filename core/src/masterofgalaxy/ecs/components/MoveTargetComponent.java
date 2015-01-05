package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class MoveTargetComponent extends Component implements Pool.Poolable {
    public Vector2 target = Vector2.Zero.cpy();
    public float speed = 0.0f;

    public Signal<Entity> destinationReached = new Signal<Entity>();

    @Override
    public void reset() {
        target.set(Vector2.Zero);
        speed = 0.0f;
        destinationReached.removeAllListeners();
    }
}
