package masterofgalaxy.world.picking;

import com.badlogic.ashley.core.Entity;
import masterofgalaxy.ecs.components.DockableComponent;
import masterofgalaxy.ecs.components.EntityTargetComponent;
import masterofgalaxy.ecs.components.Mappers;

class PickFleetToStar implements PickStrategy {
    @Override
    public void pick(final PickLogic pickLogic, Entity picked) {
        Entity fleet = pickLogic.getSelectedEntity();
        final Entity target = picked;

        EntityTargetComponent entityTarget = Mappers.entityTarget.get(fleet);

        DockableComponent dock = Mappers.dockable.get(fleet);
        if (dock != null) {
            if (dock.dockedAt == target) {
                if (entityTarget.target != null) {
                    entityTarget.target = null;
                } else {
                    pickLogic.setSelection(target);
                }
                return;
            }
        }
        entityTarget.target = target;
    }
}
