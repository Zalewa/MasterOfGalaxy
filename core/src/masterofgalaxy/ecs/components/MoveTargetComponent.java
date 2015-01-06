package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class MoveTargetComponent extends Component implements Pool.Poolable {
    private Vector2 target = Vector2.Zero.cpy();
    private boolean destinationReachedDispatched = false;
    public float speed = 0.0f;

    public Signal<Entity> destinationReached = new Signal<Entity>();

    @Override
    public void reset() {
        target.set(Vector2.Zero);
        speed = 0.0f;
        destinationReachedDispatched = false;
        destinationReached.removeAllListeners();
    }

    public Vector2 getTarget() {
        return target;
    }

    public void setTarget(Vector2 target) {
        this.target.set(target);
        destinationReachedDispatched = false;
    }

    public boolean isDestinationReachedDispatched() {
        return destinationReachedDispatched;
    }

    public void setDestinationReachedDispatched(boolean destinationReachedDispatched) {
        this.destinationReachedDispatched = destinationReachedDispatched;
    }
}
