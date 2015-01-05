package masterofgalaxy.world.picking;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.math.Vector2;
import masterofgalaxy.ecs.components.DockComponent;
import masterofgalaxy.ecs.components.EntityTargetComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.MoveTargetComponent;

class PickFleetToStar implements PickStrategy {
    @Override
    public void pick(final PickLogic pickLogic, Entity picked) {
        Entity fleet = pickLogic.getSelectedEntity();
        final Entity target = picked;

        EntityTargetComponent entityTarget = Mappers.entityTarget.get(fleet);

        DockComponent dock = Mappers.dock.get(fleet);
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
