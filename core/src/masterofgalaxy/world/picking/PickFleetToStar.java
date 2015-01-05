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

        DockComponent dock = Mappers.dock.get(fleet);
        if (dock != null) {
            if (dock.dockedAt == target) {
                pickLogic.setSelection(target);
                return;
            }
            Vector2 startingPos = new Vector2(Mappers.body.get(dock.dockedAt).getPosition());
            fleet.remove(DockComponent.class);
            Mappers.body.get(fleet).setPosition(startingPos);
        }

        EntityTargetComponent entityTarget = Mappers.entityTarget.get(fleet);
        entityTarget.target = target;

        MoveTargetComponent moveTarget = Mappers.moveTarget.get(fleet);
        if (moveTarget == null) {
            moveTarget = pickLogic.getScreen().getEntityEngine().createComponent(MoveTargetComponent.class);
            moveTarget.speed = 100.0f;
            fleet.add(moveTarget);
        }
        moveTarget.setTarget(Mappers.body.get(target).getPosition());

        moveTarget.destinationReached.add(new Listener<Entity>() {
            @Override
            public void receive(Signal<Entity> signal, Entity object) {
                DockComponent dock = pickLogic.getScreen().getEntityEngine().createComponent(DockComponent.class);
                dock.dockedAt = target;
                object.add(dock);
                object.remove(MoveTargetComponent.class);
                Mappers.entityTarget.get(object).target = null;
                signal.remove(this);

            }
        });
    }
}
