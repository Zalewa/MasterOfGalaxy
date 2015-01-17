package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import masterofgalaxy.ecs.components.FleetComponent;
import masterofgalaxy.ecs.components.Mappers;

public class FleetMerger {
    private Entity target;
    private Entity other;
    private WorldScreen screen;

    public FleetMerger(WorldScreen screen, Entity target, Entity other) {
        this.target = target;
        this.other = other;
        this.screen = screen;
    }

    public void merge() {
        FleetComponent targetComponent = Mappers.fleet.get(target);
        FleetComponent otherComponent = Mappers.fleet.get(other);
        for (FleetComponent.Ship ship : otherComponent.ships) {
            targetComponent.addShips(ship.design, ship.count);
        }
        otherComponent.ships.clear();
        screen.getEntityEngine().removeEntity(other);
    }
}
