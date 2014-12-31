package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.utils.Pool;

public class NameComponent extends Component implements Pool.Poolable {
    private String name;

    public Signal<String> nameChanged = new Signal<String>();

    @Override
    public void reset() {
        name = "";
        nameChanged.removeAllListeners();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        nameChanged.dispatch(name);
    }
}
