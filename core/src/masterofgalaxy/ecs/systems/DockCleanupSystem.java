package masterofgalaxy.ecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import masterofgalaxy.ecs.components.DockComponent;
import masterofgalaxy.ecs.components.DockableComponent;
import masterofgalaxy.ecs.components.Mappers;

public class DockCleanupSystem extends EntitySystem {
    private DockCleanup dockCleanup = new DockCleanup();

    @Override
    public void addedToEngine(Engine engine) {
        engine.addEntityListener(dockCleanup);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(dockCleanup);
    }

    private class DockCleanup implements EntityListener {
        @Override
        public void entityAdded(Entity entity) {

        }

        @Override
        public void entityRemoved(Entity entity) {
            DockableComponent dockable = Mappers.dockable.get(entity);
            if (dockable != null) {
                DockComponent dock = Mappers.dock.get(dockable.dockedAt);
                if (dock != null) {
                    dock.dockedEntities.removeValue(entity, true);
                }
            }
        }
    }
}
