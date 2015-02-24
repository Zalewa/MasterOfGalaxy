package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import masterofgalaxy.ecs.components.Mappers;

public class PlayerOwnerControl {
    public static boolean canControl(WorldScreen screen, Entity entity) {
        return isOwnedByCurrentPlayer(screen, entity) || canControlOthers(screen);
    }

    private static boolean canControlOthers(WorldScreen screen) {
        return screen.getDebugMode().isControlOtherPlayers();
    }

    private static boolean isOwnedByCurrentPlayer(WorldScreen screen, Entity entity) {
        return Mappers.playerOwner.get(entity).getOwner() == screen.getCurrentPlayer();
    }
}
