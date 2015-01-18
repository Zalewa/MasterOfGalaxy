package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import masterofgalaxy.ecs.components.*;

public class StarColonizer {
    public static boolean canColonize(WorldScreen worldScreen, Entity star) {
        if (Mappers.colony.has(star)) {
            return false;
        }
        return getColonizationFleetForCurrentPlayer(worldScreen, star) != null;
    }

    public static void colonize(WorldScreen worldScreen, Entity star) {
        Entity fleet = getColonizationFleetForCurrentPlayer(worldScreen, star);
        FleetComponent fleetComponent = Mappers.fleet.get(fleet);
        fleetComponent.destroyOneColonizer();

        ColonyComponent colony = worldScreen.getEntityEngine().createComponent(ColonyComponent.class);
        colony.entity = star;
        colony.state.population = 1.0f;
        star.add(colony);

        Mappers.playerOwner.get(star).setOwner(worldScreen.getCurrentPlayer());

        if (!fleetComponent.hasAnyShips()) {
            worldScreen.getEntityEngine().removeEntity(fleet);
        }
    }

    private static Entity getColonizationFleetForCurrentPlayer(WorldScreen worldScreen, Entity star) {
        DockComponent dock = Mappers.dock.get(star);
        for (Entity entity : dock.dockedEntities) {
            FleetComponent fleet = Mappers.fleet.get(entity);
            PlayerOwnerComponent owner = Mappers.playerOwner.get(entity);
            if (fleet != null && fleet.canColonize() && owner != null && owner.getOwner() == worldScreen.getCurrentPlayer()) {
                return entity;
            }
        }
        return null;
    }
}
