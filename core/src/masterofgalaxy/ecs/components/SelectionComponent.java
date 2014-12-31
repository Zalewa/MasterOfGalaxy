package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class SelectionComponent extends Component implements Pool.Poolable {
    public Array<Entity> selection = new Array<Entity>();
    public Rectangle boundingBox = new Rectangle();

    @Override
    public void reset() {
        selection.clear();
        boundingBox.set(0.0f, 0.0f, 0.0f, 0.0f);
    }
}
