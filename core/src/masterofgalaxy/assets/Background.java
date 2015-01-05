package masterofgalaxy.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;

public class Background {
    public final static AssetDescriptor<Texture> seamlessSpace;

    static {
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;

        seamlessSpace = new AssetDescriptor<Texture>("background/seamless_space_0.png", Texture.class, param);
    }

    public static void loadAll(AssetManager manager) {
        manager.load(seamlessSpace);
    }
}
