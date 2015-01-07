package masterofgalaxy.assets.actors;

import com.badlogic.gdx.assets.AssetManager;

public class ActorAssets {
    public PlanetClasses planetClasses = new PlanetClasses();
    public StarClasses starClasses = new StarClasses();
    public PlayerColors playerColors = new PlayerColors();

    public void loadAssets(AssetManager assetManager) {
        planetClasses.loadTextures(assetManager);
    }
}
