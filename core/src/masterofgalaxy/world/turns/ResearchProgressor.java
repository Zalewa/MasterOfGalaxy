package masterofgalaxy.world.turns;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import masterofgalaxy.assets.tech.TechKnowledge;
import masterofgalaxy.ecs.components.ColonyComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.PlayerOwnerComponent;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.world.WorldScreen;

public class ResearchProgressor {
    private WorldScreen screen;

    public ResearchProgressor(WorldScreen screen) {
        this.screen = screen;
    }

    public void progress() {
        for (int i = 0; i < screen.getWorld().getPlayers().size; ++i) {
            progressPlayer(screen.getWorld().getPlayers().get(i));
        }
    }

    private void progressPlayer(Player player) {
        int researchPoints = collectResearchPoints(player);
        TechKnowledge knowledge = player.getState().getTechKnowledge();
        knowledge.progressResearch(player.getTechTree(), researchPoints);
    }

    private int collectResearchPoints(Player player) {
        float researchPoints = 0.0f;
        ImmutableArray<Entity> entities = screen.getEntityEngine().getEntitiesFor(Family.getFor(ColonyComponent.class));
        for (int i = 0; i < entities.size(); ++i) {
            PlayerOwnerComponent owner = Mappers.playerOwner.get(entities.get(i));
            if (owner.getOwner() == player) {
                ColonyComponent colony = Mappers.colony.get(entities.get(i));
                researchPoints += colony.getResearchPoints();
            }
        }
        return (int)researchPoints;
    }
}
