package masterofgalaxy.assets.actors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import masterofgalaxy.assets.i18n.I18NJsonReader;
import masterofgalaxy.gamestate.ships.ShipHull;
import masterofgalaxy.gamestate.ships.ShipModule;

import java.util.LinkedHashMap;
import java.util.Map;

public class Ships {
    public Array<ShipHull> hulls = new Array<ShipHull>();
    private Map<ShipModuleType, Array<ShipModule>> modules = new LinkedHashMap<ShipModuleType, Array<ShipModule>>();
    private String localizationBundleName;

    public Ships() {
        initShipModuleMap();
    }

    private void initShipModuleMap() {
        for (ShipModuleType type : ShipModuleType.values()) {
            modules.put(type, new Array<ShipModule>());
        }
    }

    public void load(FileHandle file) {
        JsonReader reader = new JsonReader();
        JsonValue jsonRoot = reader.parse(file);
        localizationBundleName = I18NJsonReader.loadNamedBundle(jsonRoot, file.type());
        parseData(jsonRoot);
    }

    private void parseData(JsonValue jsonRoot) {
        parseHulls(jsonRoot.get("hull"));
        for (ShipModuleType type : ShipModuleType.values()) {
            parseShipModules(type, jsonRoot);
        }
    }

    private void parseHulls(JsonValue json) {
        for (int i = 0; i < json.size; ++i) {
            hulls.add(parseHull(json.get(i)));
        }
    }

    private ShipHull parseHull(JsonValue json) {
        ShipHull hull = new ShipHull();
        hull.localizationBundle = localizationBundleName;
        hull.name = json.name();
        hull.cost = json.getFloat("cost");
        hull.size = json.getFloat("size");
        return hull;
    }

    private void parseShipModules(ShipModuleType type, JsonValue jsonRoot) {
        Array<ShipModule> result = new Array<ShipModule>();
        JsonValue jsonModules = jsonRoot.get(type.lowerCaseName());
        if (jsonModules != null) {
            for (int i = 0; i < jsonModules.size; ++i) {
                ShipModule module = parseShipModule(jsonModules.get(i));
                module.type = type;
                result.add(module);
            }
        }
        modules.get(type).addAll(result);
    }

    private ShipModule parseShipModule(JsonValue json) {
        ShipModule module = new ShipModule();
        module.name = json.name();
        module.cost = json.getFloat("cost");
        module.size = json.getFloat("size");

        module.speed = json.getFloat("speed", 0.0f);
        module.travelDistanceIncrease = json.getFloat("travelDistanceIncrease", 0.0f);
        module.canColonize = json.getBoolean("canColonize", false);

        return module;
    }

    public ShipHull findHull(String name) {
        for (ShipHull hull : hulls) {
            if (hull.name.equals(name)) {
                return hull;
            }
        }
        return null;
    }

    public ShipModule findModule(ShipModuleType type, String name) {
        Array<ShipModule> modules = this.modules.get(type);
        for (ShipModule module : modules) {
            if (module.name.equals(name)) {
                return module;
            }
        }
        return null;
    }
}
