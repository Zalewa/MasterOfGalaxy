package masterofgalaxy.world.turns;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import masterofgalaxy.ecs.components.ColonyComponent;
import masterofgalaxy.ecs.components.FleetComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.entities.FleetFactory;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.world.Docker;
import masterofgalaxy.world.WorldScreen;
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
        growShipyardBuild(colony);

        buildShips(entity);
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

    private void growShipyardBuild(ColonyComponent colony) {
        colony.shipyard.investment += colony.getShipProduction();
    }

    private void buildShips(Entity colonyEntity) {
        ColonyComponent colony = Mappers.colony.get(colonyEntity);
        if (colony.shipyard.constructedShip == null) {
            return;
        }

        Player owner = Mappers.playerOwner.get(colonyEntity).getOwner();

        int builtShips = MathUtils.floor(colony.shipyard.investment / colony.shipyard.constructedShip.getCost());
        if (builtShips > 0) {
            Entity fleet = FleetFactory.build(screen.getGame(), screen.getEntityEngine());
            Docker.dock(screen, fleet, colonyEntity);
            Mappers.playerOwner.get(fleet).setOwner(owner);
            FleetComponent fleetComponent = Mappers.fleet.get(fleet);
            fleetComponent.addShips(colony.shipyard.constructedShip, builtShips);
            colony.shipyard.investment -= builtShips * colony.shipyard.constructedShip.getCost();
        }
    }


    private void growBases(ColonyComponent colony) {
        colony.state.defenseBases += colony.getDefenseBasesGrowthRate();
    }
}
