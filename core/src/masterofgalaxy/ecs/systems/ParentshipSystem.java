package masterofgalaxy.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.ParentshipComponent;

public class ParentshipSystem extends EntitySystem {
    private EntityListener listener;

    @Override
    public void addedToEngine(final Engine engine) {
        listener = new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
            }

            @Override
            public void entityRemoved(Entity entity) {
                removeParentship(engine, entity);
            }
        };
        engine.addEntityListener(Family.getFor(ParentshipComponent.class), listener);
    }

    private void removeParentship(Engine engine, Entity entity) {
        ParentshipComponent parentship = Mappers.parentship.get(entity);
        destroyChildren(engine, parentship.getChildren());
        if (parentship.getParent() != null) {
            removeFromParent(parentship.getParent(), entity);
        }
        parentship.reset();
    }

    private void removeFromParent(Entity parent, Entity entity) {
        if (parent != null) {
            ParentshipComponent parentship = Mappers.parentship.get(parent);
            parentship.unlinkChild(entity);
        }
    }

    private void destroyChildren(Engine engine, ImmutableArray<Entity> children) {
        for (int i = 0; i < children.size(); ++i) {
            Entity child = children.get(i);
            ParentshipComponent parentship = Mappers.parentship.get(child);
            if (parentship != null) {
                destroyChildren(engine, parentship.getChildren());
            }
            engine.removeEntity(child);
            parentship.reset();
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(listener);
    }
}
