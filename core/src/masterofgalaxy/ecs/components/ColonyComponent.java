package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.Race;
import masterofgalaxy.world.stars.Planet;

public class ColonyComponent extends Component implements Pool.Poolable {
    public final ColonyState state = new ColonyState();
    public Entity entity;

    @Override
    public void reset() {
        entity = null;
        state.population = 0.0f;
        state.factories = 0.0f;
    }

    public float getGrowthRate() {
        return getPlanet().getGrowthRateMultiplier() * getRace().getPopulationGrowthRate();
    }

    public float getProduction() {
        return getMannedFactories() * getPlanet().getProductionMultiplier() * getRace().getProductionRate();
    }

    public int getMannedFactories() {
        return Math.min((int)state.population, (int)state.factories);
    }

    public float getMaxResearchPoints() {
        return getProduction() * getPlanet().getResearchMultiplier() * getRace().getResearchRate();
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
}
