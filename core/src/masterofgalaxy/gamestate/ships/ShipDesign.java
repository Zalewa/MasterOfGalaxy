package masterofgalaxy.gamestate.ships;

import com.badlogic.gdx.utils.Array;
import masterofgalaxy.assets.actors.ShipModuleType;

public class ShipDesign {
    private String name = "";
    private ShipHull hull = new ShipHull();
    private Array<ShipModule> modules = new Array<ShipModule>();

    public void addModule(ShipModule module) {
        if (module.type.isTypeExclusive()) {
            removeAllModulesOfType(module.type);
        }
        if (module.type.isOnlyOneUnitAllowed()) {
            removeAllModulesOfTypeAndName(module.type, module.name);
        }
        modules.add(module);
    }

    public void removeAllModulesOfType(ShipModuleType type) {
        for (int i = modules.size - 1; i >= 0; --i) {
            if (modules.get(i).type == type) {
                modules.removeIndex(i);
            }
        }
    }

    private void removeAllModulesOfTypeAndName(ShipModuleType type, String name) {
        for (int i = modules.size - 1; i >= 0; --i) {
            if (modules.get(i).type == type && modules.get(i).name.equals(name)) {
                modules.removeIndex(i);
            }
        }
    }

    public float getSpeed() {
        float result = 0.0f;
        for (ShipModule module : modules) {
            result += module.speed;
        }
        return result;
    }

    public float getTravelDistanceIncrease() {
        float result = 0.0f;
        for (ShipModule module : modules) {
            result += module.travelDistanceIncrease;
        }
        return result;
    }

    public boolean canColonize() {
        for (ShipModule module : modules) {
            if (module.canColonize) {
                return true;
            }
        }
        return false;
    }

    public ShipHull getHull() {
        return hull;
    }

    public void setHull(ShipHull hull) {
        this.hull = hull;
    }

    public float getFreeSpace() {
        return hull.size - getTakenSpace();
    }

    public float getTakenSpace() {
        float takenSpace = 0.0f;
        for (ShipModule module : modules) {
            takenSpace += module.size;
        }
        return takenSpace;
    }

    public float getCost() {
        float cost = hull.cost;
        for (ShipModule module : modules) {
            cost += module.cost;
        }
        return cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
