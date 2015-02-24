package masterofgalaxy.assets.actors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import masterofgalaxy.assets.i18n.I18NJsonReader;
import masterofgalaxy.exceptions.InvalidInputTypeException;
import masterofgalaxy.gamestate.Homeworld;
import masterofgalaxy.gamestate.Race;

public class Races {
    public static Race findRaceByName(Array<Race> races, String name) {
        for (Race race : races) {
            if (race.getName().equals(name)) {
                return race;
            }
        }
        return null;
    }

    public Array<Race> races = new Array<Race>();
    private String localizationBundleName;

    public void load(FileHandle file) {
        JsonReader reader = new JsonReader();
        JsonValue jsonRoot = reader.parse(file);
        localizationBundleName = I18NJsonReader.loadNamedBundle(jsonRoot, file.type());
        JsonValue races = jsonRoot.get("races");
        if (races == null || !races.isObject()) {
            throw new InvalidInputTypeException(file.path() + " is not a races file");
        }
        for (int i = 0; i < races.size; ++i) {
            JsonValue raceDef = races.get(i);
            String name = raceDef.name();

            Race race = new Race();
            race.setName(name);
            race.setLocalizationBase(raceDef.getString("localizationBase"));
            race.setLocalizationBundleName(localizationBundleName);
            race.setPopulationGrowthRate(raceDef.getFloat("populationGrowthRate"));
            race.setProductionRate(raceDef.getFloat("productionRate"));
            race.setResearchRate(raceDef.getFloat("researchRate"));
            race.setHomeworld(parseHomeworld(raceDef.get("homeworld")));

            this.races.add(race);
        }
    }

    private Homeworld parseHomeworld(JsonValue homeworldDef) {
        Homeworld homeworld = new Homeworld();
        homeworld.setName(homeworldDef.getString("name"));
        homeworld.setPlanetType(homeworldDef.getString("planetType"));
        homeworld.setStarType(homeworldDef.getString("starType"));
        return homeworld;
    }
}
