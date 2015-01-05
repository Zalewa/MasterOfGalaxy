package masterofgalaxy.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import masterofgalaxy.ecs.components.EntityTargetComponent;
import masterofgalaxy.ecs.components.Mappers;

public class TargetPurgingSystem extends EntitySystem {
    private EntityListener entityListener;

    @Override
    public void addedToEngine(final Engine engine) {
        entityListener = new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {

            }

            @Override
            public void entityRemoved(Entity entity) {
                purgeEntityFromTargeters(engine, entity);
            }
        };
        engine.addEntityListener(entityListener);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(entityListener);
    }

    private void purgeEntityFromTargeters(Engine engine, Entity entity) {
        ImmutableArray<Entity> targeters = engine.getEntitiesFor(Family.getFor(EntityTargetComponent.class));
        for (int i = 0; i < targeters.size(); ++i) {
            EntityTargetComponent targetComponent = Mappers.entityTarget.get(targeters.get(i));
            if (targetComponent.target == entity) {
                targetComponent.target = null;
            }
        }
    }
}
