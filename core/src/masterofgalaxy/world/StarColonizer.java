package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import masterofgalaxy.ecs.components.DockComponent;
import masterofgalaxy.ecs.components.FleetComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.PlayerOwnerComponent;

public class StarColonizer {
    public static boolean canColonize(WorldScreen worldScreen, Entity star) {
        DockComponent dock = Mappers.dock.get(star);
        for (Entity entity : dock.dockedEntities) {
            FleetComponent fleet = Mappers.fleet.get(entity);
            PlayerOwnerComponent owner = Mappers.playerOwner.get(entity);
            if (fleet != null && fleet.canColonize() && owner != null && owner.getOwner() == worldScreen.getCurrentPlayer()) {
                return true;
            }
        }
        return false;
    }
}
