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
        ImmutableArray<Entity> docks = screen.getEntityEngine().getEntitiesFor(
                Family.getFor(DockComponent.class));
        for (int i = 0; i < docks.size(); ++i) {
            DockComponent dock = Mappers.dock.get(docks.get(i));
            mergeDock(dock);
        }
    }

    public boolean canMerge(DockComponent dock) {
        for (int i = 0; i < dock.dockedEntities.size; ++i) {
            Entity entity = dock.dockedEntities.get(i);
            if (isMergable(entity)) {
                for (int j = i; j < dock.dockedEntities.size; ++j) {
                    Entity other = dock.dockedEntities.get(j);
                    if (isMergable(other)) {
                        if (Mappers.playerOwner.get(entity).getOwner() == Mappers.playerOwner.get(other).getOwner()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void mergeDock(DockComponent dock) {
        for (int i = 0; i < dock.dockedEntities.size; ++i) {
            Entity fleet = dock.dockedEntities.get(i);
            if (!Mappers.fleet.has(fleet)) {
                continue;
            }
            for (int j = dock.dockedEntities.size - 1; j >= i; --j) {
                Entity otherDocked = dock.dockedEntities.get(j);
                tryMerge(fleet, otherDocked);
            }
        }
    }

    private void tryMerge(Entity targetFleet, Entity otherDocked) {
        if (targetFleet == otherDocked) {
            return;
        }
        if (!Mappers.fleet.has(otherDocked)) {
            return;
        }
        if (Mappers.playerOwner.get(targetFleet).getOwner() != Mappers.playerOwner.get(otherDocked).getOwner()) {
            return;
        }
        FleetMerger merger = new FleetMerger(screen, targetFleet, otherDocked);
        merger.merge();
        if (screen.getPickLogic().getSelectedEntity() == otherDocked) {
            screen.getPickLogic().setSelection(targetFleet);
        }
    }

    private boolean isMergable(Entity entity) {
        return Mappers.fleet.has(entity);
    }
}
