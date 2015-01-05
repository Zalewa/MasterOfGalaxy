package masterofgalaxy.world.picking;

import com.badlogic.ashley.core.Entity;
import masterofgalaxy.ecs.components.Mappers;

class PickStrategyFactory {
    public static PickStrategy build(PickLogic pickLogic, Entity picked) {
        Entity selected = pickLogic.getSelectedEntity();
        if (selected != null && picked != null) {
            if (Mappers.fleet.has(selected) && Mappers.star.has(picked)) {
                return new PickFleetToStar();
            }
        }
        return new PickSelect();
    }
}
