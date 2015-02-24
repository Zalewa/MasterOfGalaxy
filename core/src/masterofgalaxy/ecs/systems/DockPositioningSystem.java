package masterofgalaxy.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import masterofgalaxy.ecs.components.BodyComponent;
import masterofgalaxy.ecs.components.DockableComponent;
import masterofgalaxy.ecs.components.Mappers;

import java.util.LinkedHashMap;
import java.util.Map;

public class DockPositioningSystem extends IteratingSystem {
    private Map<Entity, Integer> dockShifts = new LinkedHashMap<Entity, Integer>();

    public DockPositioningSystem() {
        super(Family.getFor(BodyComponent.class, DockableComponent.class));
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent body = Mappers.body.get(entity);
        DockableComponent dock = Mappers.dockable.get(entity);

        int dockShift = 0;
        if (dockShifts.containsKey(dock.dockedAt)) {
            dockShift = dockShifts.get(dock.dockedAt);
        }

        BodyComponent dockBody = Mappers.body.get(dock.dockedAt);
        Rectangle dockBounds = dockBody.getBounds();
        body.setPosition(dockBounds.x + dockBounds.width, dockBounds.y + dockBounds.height + dockShift);
        dockShift += body.getSize().y;

        dockShifts.put(dock.dockedAt, dockShift);
    }

    @Override
    public void update(float deltaTime) {
        dockShifts.clear();
        super.update(deltaTime);
    }
}
