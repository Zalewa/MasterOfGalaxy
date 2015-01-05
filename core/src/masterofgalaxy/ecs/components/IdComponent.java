package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class IdComponent extends Component implements Pool.Poolable {
    public String id = "";

    @Override
    public void reset() {
        id = "";
    }
}
