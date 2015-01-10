package masterofgalaxy.assets.actors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import masterofgalaxy.assets.i18n.I18NJsonReader;
import masterofgalaxy.exceptions.InvalidInputTypeException;
import masterofgalaxy.gamestate.Race;
import masterofgalaxy.world.stars.PlanetClass;

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

            this.races.add(race);
        }
    }
}
