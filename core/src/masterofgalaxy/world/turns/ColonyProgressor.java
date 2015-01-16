package masterofgalaxy.world.turns;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import masterofgalaxy.ecs.components.ColonyComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.world.WorldScreen;
import masterofgalaxy.world.stars.MainResourceDistribution;
import masterofgalaxy.world.stars.MainResourceDistribution.ResourceId;

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

        growPopulation(colony);
        growFactories(colony);
        growBases(colony);
    }

    private void growPopulation(ColonyComponent colony) {
        colony.state.population += colony.getPopulationGrowthRate();
        colony.state.population = Math.min(colony.getMaxPopulation(), colony.state.population);
        if (colony.isMaxPopulation()) {
            colony.state.mainResourceDistribution.clearAndDistributeElsewhere(ResourceId.Ecology, ResourceId.Research);
        }
    }

    private void growFactories(ColonyComponent colony) {
        colony.state.factories += colony.getFactoriesGrowthRate();
        colony.state.factories = Math.min(colony.getMaxFactories(), colony.state.factories);
        if (colony.isMaxFactories()) {
            colony.state.mainResourceDistribution.clearAndDistributeElsewhere(ResourceId.Industry, ResourceId.Research);
        }
    }

    private void growBases(ColonyComponent colony) {
        colony.state.defenseBases += colony.getDefenseBasesGrowthRate();
    }
}
