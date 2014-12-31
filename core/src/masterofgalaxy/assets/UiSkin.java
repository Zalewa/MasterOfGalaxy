package masterofgalaxy.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UiSkin {
    public final static AssetDescriptor<Skin> skin = new AssetDescriptor<Skin>("ui/uiskin.json", Skin.class);

    public static void loadAll(AssetManager manager) {
        manager.load(skin);
    }
}
