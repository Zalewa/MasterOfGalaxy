package masterofgalaxy.assets.actors;

import com.badlogic.gdx.assets.AssetManager;
import masterofgalaxy.world.stars.PlanetClass;

public class ActorAssets {
    public PlanetClasses planetClasses = new PlanetClasses();
    public StarClasses starClasses = new StarClasses();

    public void loadAssets(AssetManager assetManager) {
        planetClasses.loadTextures(assetManager);
    }
}
