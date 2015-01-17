package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import masterofgalaxy.exceptions.InvalidArgumentException;
import masterofgalaxy.gamestate.ships.ShipDesign;

import java.text.MessageFormat;

public class FleetComponent extends Component implements Pool.Poolable {
    public Array<Ship> ships = new Array<Ship>();

    @Override
    public void reset() {
        ships.clear();
    }

    public float getSpeedInParsecsPerTurn() {
        float result = Float.MAX_VALUE;
        for (Ship ship : ships) {
            if (ship.count > 0) {
                result = Math.min(result, ship.design.getSpeed());
            }
        }
        return result;
    }

    public Ship getOrCreateShipOfDesign(ShipDesign design) {
        if (design == null) {
            throw new InvalidArgumentException("tried to add 'null' ShipDesign to FleetComponent");
        }
        for (Ship ship : ships) {
            if (ship.design == design) {
                return ship;
            }
        }
        Ship ship = new Ship(design);
        ships.add(ship);
        return ship;
    }

    public int getAmountOfShips(ShipDesign design) {
        for (Ship ship : ships) {
            if (ship.design == design) {
                return ship.count;
            }
        }
        return 0;
    }

    public boolean hasAnyShips() {
        for (Ship ship : ships) {
            if (ship.count > 0) {
                return true;
            }
        }
        return false;
    }

    public void addShips(ShipDesign design, int amount) {
        getOrCreateShipOfDesign(design).count += amount;
    }

    public static class Ship {
        public int count;
        public ShipDesign design;

        public Ship(ShipDesign design) {
            this.design = design;
        }
    }
}
