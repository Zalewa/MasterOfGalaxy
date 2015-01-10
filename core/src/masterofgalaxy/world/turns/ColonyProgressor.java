package masterofgalaxy.world.turns;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import masterofgalaxy.ecs.components.ColonyComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.PlayerOwnerComponent;
import masterofgalaxy.ecs.components.StarComponent;
import masterofgalaxy.world.WorldScreen;

public class ColonyProgressor {
    private WorldScreen screen;

    public ColonyProgressor(WorldScreen screen) {
        this.screen = screen;
    }

    public void progress() {
        ImmutableArray<Entity> entities = screen.getEntityEngine().getEntitiesFor(Family.getFor(ColonyComponent.class));
        for (int i = 0; i < entities.size(); ++i) {
            progressColony(entities.get(i));
        }
    }

    private void progressColony(Entity entity) {
        ColonyComponent colony = Mappers.colony.get(entity);
        colony.state.population += colony.getGrowthRate();
    }
}
