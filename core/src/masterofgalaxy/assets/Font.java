package masterofgalaxy.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Font {
    public static final AssetDescriptor<BitmapFont> starNameFont = new AssetDescriptor<BitmapFont>("ui/default.fnt", BitmapFont.class);;

    public static void loadAll(AssetManager manager) {
        manager.load(starNameFont);
    }
}
