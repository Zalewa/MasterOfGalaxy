package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.ecs.components.DockableComponent;
import masterofgalaxy.ecs.components.FleetComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.entities.FleetFactory;
import masterofgalaxy.gamestate.ships.ShipDesign;

public class FleetSplitter {
    private Entity fleet;
    private Array<FleetComponent.Ship> shipsInNewFleet = new Array<FleetComponent.Ship>();
    private WorldScreen screen;

    public static boolean canSplitFleet(Entity fleet) {
        return Mappers.dockable.has(fleet) && Mappers.fleet.get(fleet).getTotalAmountOfShips() > 1;
    }

    public FleetSplitter(WorldScreen screen, Entity fleet) {
        this.screen = screen;
        this.fleet = fleet;
    }

    public void setShipAmount(ShipDesign design, int amount) {
        removeShip(design);
        FleetComponent.Ship ship = new FleetComponent.Ship(design);
        ship.count = amount;
        shipsInNewFleet.add(ship);
    }

    public void removeShip(ShipDesign design) {
        int index = getIndex(design);
        if (index >= 0) {
            shipsInNewFleet.removeIndex(index);
        }
    }

    public Entity split() {
        if (isCurrentFleetGoingToBeEmpty()) {
            return null;
        }
        Entity newFleet = FleetFactory.build(screen.getGame(), screen.getEntityEngine());
        FleetComponent fleetComponent = Mappers.fleet.get(fleet);
        FleetComponent newFleetComponent = Mappers.fleet.get(newFleet);
        for (int i = 0; i < fleetComponent.ships.size; ++i) {
            FleetComponent.Ship ship = fleetComponent.ships.get(i);
            int actualAmount = Math.min(ship.count, getSplitAmount(ship.design));
            fleetComponent.addShips(ship.design, -actualAmount);
            newFleetComponent.addShips(ship.design, actualAmount);
        }
        if (newFleetComponent.hasAnyShips()) {
            cloneFleetProperties(newFleet);
            return newFleet;
        } else {
            screen.getEntityEngine().removeEntity(newFleet);
            return null;
        }
    }

    private boolean isCurrentFleetGoingToBeEmpty() {
        FleetComponent fleetComponent = Mappers.fleet.get(fleet);
        for (FleetComponent.Ship ship : fleetComponent.ships) {
            if (ship.count > getSplitAmount(ship.design)) {
                return false;
            }
        }
        return true;
    }

    private void cloneFleetProperties(Entity newFleet) {
        DockableComponent dockable = Mappers.dockable.get(fleet);
        if (dockable != null) {
            Docker.dock(screen, newFleet, dockable.dockedAt);
        }
        Mappers.playerOwner.get(newFleet).setOwner(Mappers.playerOwner.get(fleet).getOwner());
        Mappers.body.get(newFleet).setState(Mappers.body.get(fleet).getState());
    }

    private int getSplitAmount(ShipDesign design) {
        int index = getIndex(design);
        if (index >= 0) {
            return shipsInNewFleet.get(index).count;
        }
        return 0;
    }

    private int getIndex(ShipDesign design) {
        for (int i = shipsInNewFleet.size - 1; i >= 0; --i) {
            if (shipsInNewFleet.get(i).design == design) {
                return i;
            }
        }
        return -1;
    }
}
