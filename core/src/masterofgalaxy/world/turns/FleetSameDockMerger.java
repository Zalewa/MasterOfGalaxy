package masterofgalaxy.world.turns;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import masterofgalaxy.ecs.components.DockComponent;
import masterofgalaxy.ecs.components.DockableComponent;
import masterofgalaxy.ecs.components.FleetComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.world.FleetMerger;
import masterofgalaxy.world.WorldScreen;

public class FleetSameDockMerger {
    private WorldScreen screen;

    public FleetSameDockMerger(WorldScreen screen) {
        this.screen = screen;
    }

    public void execute() {
        ImmutableArray<Entity> dockedFleets = screen.getEntityEngine().getEntitiesFor(
                Family.getFor(FleetComponent.class, DockableComponent.class));
        for (int i = 0; i < dockedFleets.size(); ++i) {
            Entity fleet = dockedFleets.get(i);
            DockableComponent dockable = Mappers.dockable.get(fleet);
            Entity dockEntity = dockable.dockedAt;
            DockComponent dockComponent = Mappers.dock.get(dockEntity);

            for (Entity otherDocked : dockComponent.dockedEntities) {
                tryMerge(fleet, otherDocked);
            }
        }
    }

    private void tryMerge(Entity fleet, Entity otherDocked) {
        if (fleet == otherDocked) {
            return;
        }
        if (!Mappers.fleet.has(otherDocked)) {
            return;
        }
        if (Mappers.playerOwner.get(fleet).getOwner() != Mappers.playerOwner.get(otherDocked).getOwner()) {
            return;
        }
        FleetMerger merger = new FleetMerger(screen, otherDocked, fleet);
        merger.merge();
    }
}
