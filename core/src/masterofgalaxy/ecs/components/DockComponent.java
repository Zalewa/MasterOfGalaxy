package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class DockComponent extends Component implements Pool.Poolable {
    public Array<Entity> dockedEntities = new Array<Entity>();

    @Override
    public void reset() {
        dockedEntities.clear();
    }
}
