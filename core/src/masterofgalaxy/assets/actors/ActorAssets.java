package masterofgalaxy.assets.actors;

import masterofgalaxy.assets.tech.TechIndexParser;
import masterofgalaxy.assets.tech.TechTree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

public class ActorAssets {
    public PlanetClasses planetClasses = new PlanetClasses();
    public StarClasses starClasses = new StarClasses();
    public PlayerColors playerColors = new PlayerColors();
    public Races races = new Races();
    public Ships ships = new Ships();
    public TechTree tech;

    public void loadAssets(AssetManager assetManager) {
        loadActors();
        loadTextures(assetManager);
        loadTechTree();
    }

    private void loadTextures(AssetManager assetManager) {
        planetClasses.loadTextures(assetManager);
    }

    private void loadActors() {
        planetClasses.load(Gdx.files.internal("actors/planets.json"));
        starClasses.load(Gdx.files.internal("actors/stars.json"));
        playerColors.load(Gdx.files.internal("actors/playercolors.json"));
        races.load(Gdx.files.internal("actors/races.json"));
        ships.load(Gdx.files.internal("actors/ships.json"));
    }

    private void loadTechTree() {
        tech = TechIndexParser.load(Gdx.files.internal("tech/tech.json"));
    }
}
