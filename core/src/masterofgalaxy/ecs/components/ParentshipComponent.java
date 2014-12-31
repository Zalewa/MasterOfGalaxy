package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import masterofgalaxy.exceptions.InvalidOperationException;

public class ParentshipComponent extends Component implements Pool.Poolable {
    private Entity parent;
    private Array<Entity> children = new Array<Entity>();

    @Override
    public void reset() {
        parent = null;
        children.clear();
    }

    public void linkChild(Entity current, Entity entity) {
        ParentshipComponent parentship = Mappers.parentship.get(entity);
        if (parentship == null) {
            throw new InvalidOperationException("entity that is to be a child must have a ParentshipComponent");
        }
        if (parentship.parent != null) {
            ParentshipComponent otherParentship = Mappers.parentship.get(parentship.parent);
            otherParentship.unlinkChild(entity);
        }
        parentship.parent = current;
        children.add(entity);
    }

    public void unlinkChild(Entity entity) {
        ParentshipComponent parentship = Mappers.parentship.get(entity);
        if (parentship == null) {
            throw new InvalidOperationException("child that is to be unlinked must have a ParentshipComponent");
        }
        children.removeValue(entity, true);
        parentship.parent = null;
    }

    public Entity getParent() {
        return parent;
    }

    public ImmutableArray<Entity> getChildren() {
        return new ImmutableArray<Entity>(children);
    }
}
