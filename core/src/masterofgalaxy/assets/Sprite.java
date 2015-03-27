package masterofgalaxy.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;

public class Sprite {
    public final static AssetDescriptor<Texture> fleet;
    public final static AssetDescriptor<Texture> star;
    public final static AssetDescriptor<Texture> selection;
    public final static AssetDescriptor<Texture> padlockOpen;
    public final static AssetDescriptor<Texture> padlockClosed;

    static {
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;

        fleet = new AssetDescriptor<Texture>("sprite/fleet.png", Texture.class, param);
        star = new AssetDescriptor<Texture>("sprite/star.png", Texture.class, param);
        selection = new AssetDescriptor<Texture>("sprite/selection.png", Texture.class, param);
        padlockOpen = new AssetDescriptor<Texture>("sprite/padlock_open.png", Texture.class, param);
        padlockClosed = new AssetDescriptor<Texture>("sprite/padlock_closed.png", Texture.class, param);
    }

    public static void loadAll(AssetManager manager) {
        manager.load(fleet);
        manager.load(star);
        manager.load(selection);
        manager.load(padlockOpen);
        manager.load(padlockClosed);
    }
}
