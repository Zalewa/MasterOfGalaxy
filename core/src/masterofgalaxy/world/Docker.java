package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import masterofgalaxy.ecs.components.DockComponent;
import masterofgalaxy.ecs.components.DockableComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.exceptions.InvalidOperationException;

public class Docker {
    public static void dock(WorldScreen screen, Entity dockee, Entity dock) {
        if (!Mappers.dock.has(dock)) {
            throw new InvalidOperationException("dock entity has no 'Dock' component");
        }

        undock(dockee);

        DockableComponent dockable = screen.getEntityEngine().createComponent(DockableComponent.class);
        dockable.dockedAt = dock;
        dockee.add(dockable);

        DockComponent dockComponent = Mappers.dock.get(dock);
        dockComponent.dockedEntities.add(dockee);
    }

    public static void undock(Entity dockee) {
        DockableComponent dockable = Mappers.dockable.get(dockee);
        if (dockable != null) {
            Entity dock = dockable.dockedAt;
            DockComponent dockComponent = Mappers.dock.get(dock);
            dockComponent.dockedEntities.removeValue(dockee, true);

            dockee.remove(DockableComponent.class);
        }
    }
}
