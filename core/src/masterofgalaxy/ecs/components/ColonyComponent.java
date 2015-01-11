package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import masterofgalaxy.AskForFloat;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.Race;
import masterofgalaxy.world.stars.ColonyState;
import masterofgalaxy.world.stars.MainResourceDistribution.ResourceId;
import masterofgalaxy.world.stars.Planet;

public class ColonyComponent extends Component implements Pool.Poolable {
    public final ColonyState state = new ColonyState();
    public Entity entity;

    public ColonyComponent() {
        state.mainResourceDistribution.setMinAmountAsker(ResourceId.Ecology, new AskForFloat() {
            @Override
            public float ask() {
                return getMinimumEcologyPercentage();
            }
        });
        reset();
    }

    @Override
    public void reset() {
        entity = null;
        state.population = 0.0f;
        state.factories = 0.0f;
        state.mainResourceDistribution.setIndustry(1.0f);
    }

    public float getPopulationGrowthRate() {
        return getNaturalGrowthRate() + getEcologyGrowthBonus();
    }

    private float getNaturalGrowthRate() {
        return getPlanet().getGrowthRateMultiplier() * getRace().getPopulationGrowthRate();
    }

    private float getEcologyGrowthBonus() {
        return getEcologyRatioAboveMin() * getProduction() / getOwner().getProductionCostPerPopulationGrowth();
    }

    private float getEcologyRatioAboveMin() {
        return state.mainResourceDistribution.getEcology() - getMinimumEcologyPercentage();
    }

    public float getFactoriesGrowthRate() {
        return state.mainResourceDistribution.getIndustry() * getProduction() / getOwner().getProductionCostPerFactory();
    }

    public float getProduction() {
        return getPlanet().getProductionMultiplier() * (getProductionFromMannedFactories() + getProductionFromJoblessPopulation());
    }

    private float getProductionFromJoblessPopulation() {
        return getJoblessPopulation() * getOwner().getProductionPerJoblessPopulation();
    }

    private int getJoblessPopulation() {
        return (int)Math.max(state.population - state.factories, 0.0f);
    }

    private float getProductionFromMannedFactories() {
        return getMannedFactories() * getOwner().getProductionPerMannedFactory();
    }

    public int getMannedFactories() {
        return Math.min((int)state.population, (int)state.factories);
    }

    public float getMaxResearchPoints() {
        return getProduction() * getPlanet().getResearchMultiplier() * getRace().getResearchRate();
    }

    public float getMaxPopulation() {
        return getPlanet().getMaxPopulation();
    }

    public float getMaxFactories() {
        return getMaxPopulation() * getOwner().getFactoryPopulationCapacity();
    }

    private Race getRace() {
        return getOwner().getRace();
    }

    private Player getOwner() {
        return Mappers.playerOwner.get(entity).getOwner();
    }

    private Planet getPlanet() {
        return getStarComponent().planet;
    }

    private StarComponent getStarComponent() {
        return Mappers.star.get(entity);
    }

    public float getMinimumEcologyPercentage() {
        return 0.3f;
    }
}
