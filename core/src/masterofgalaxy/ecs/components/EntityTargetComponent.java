package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class EntityTargetComponent extends Component implements Pool.Poolable {
    public Entity target;

    @Override
    public void reset() {
        target = null;
    }
}
