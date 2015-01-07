package masterofgalaxy.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import masterofgalaxy.assets.fonts.Font;

public class UiSkin {
    public static Skin skin;

    public static void loadAll(AssetManager manager) {
        Skin skin = new Skin();

        skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
        skin.add("default-font", Font.getUiFont(manager));

        skin.load(Gdx.files.internal("ui/uiskin.json"));

        UiSkin.skin = skin;
    }
}
