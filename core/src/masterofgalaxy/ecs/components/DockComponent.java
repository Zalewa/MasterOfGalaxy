package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class DockComponent extends Component implements Pool.Poolable {
    public Entity dockedAt;

    @Override
    public void reset() {
        dockedAt = null;
    }
}
