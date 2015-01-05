package masterofgalaxy.world.picking;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.math.Vector2;
import masterofgalaxy.ecs.components.DockComponent;
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

        MoveTargetComponent component = Mappers.moveTarget.get(fleet);
        if (component == null) {
            component = pickLogic.getScreen().getEntityEngine().createComponent(MoveTargetComponent.class);
            component.speed = 100.0f;
            fleet.add(component);
        }
        component.setTarget(Mappers.body.get(target).getPosition());

        component.destinationReached.add(new Listener<Entity>() {
            @Override
            public void receive(Signal<Entity> signal, Entity object) {
                DockComponent dock = pickLogic.getScreen().getEntityEngine().createComponent(DockComponent.class);
                dock.dockedAt = target;
                object.add(dock);
                object.remove(MoveTargetComponent.class);
                signal.remove(this);
            }
        });
    }
}
