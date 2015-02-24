package masterofgalaxy.world.picking;

import com.badlogic.ashley.core.Entity;

import masterofgalaxy.ecs.components.DockableComponent;
import masterofgalaxy.ecs.components.EntityTargetComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.world.PlayerOwnerControl;

class PickFleetToStar implements PickStrategy {
    @Override
    public void pick(final PickLogic pickLogic, Entity picked) {
        Entity fleet = pickLogic.getSelectedEntity();
        if (!canControlThisFleet(pickLogic, fleet)) {
            pickLogic.setSelection(picked);
            return;
        }

        EntityTargetComponent entityTarget = Mappers.entityTarget.get(fleet);

        DockableComponent dock = Mappers.dockable.get(fleet);
        if (dock != null) {
            if (dock.dockedAt == picked) {
                if (entityTarget.target != null) {
                    entityTarget.target = null;
                } else {
                    pickLogic.setSelection(picked);
                }
                return;
            }
        }
        entityTarget.target = picked;
    }

    private boolean canControlThisFleet(PickLogic pickLogic, Entity fleet) {
        return PlayerOwnerControl.canControl(pickLogic.getScreen(), fleet);
    }
}
