package masterofgalaxy.assets.fonts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.LocalizationEntry;

import java.util.Map;

public class Font {
    private static Map<String, String> fontRoles;

    public static BitmapFont getStarNameFont(AssetManager manager) {
        return manager.get(fontRoles.get("star-name"), BitmapFont.class);
    }

    public static BitmapFont getUiFont(AssetManager manager) {
        return manager.get(fontRoles.get("ui"), BitmapFont.class);
    }

    public static void loadAll(AssetManager manager) {
        FontRoleParser roleParser = new FontRoleParser();
        fontRoles = roleParser.parse(Gdx.files.internal("fonts/fontdef.json"));

        FontDefParser parser = new FontDefParser();
        Map<String, FontDef> fontDefs = parser.parse(Gdx.files.internal("fonts/fontdef.json"));

        setupFontResolver(manager);

        for (FontDef def : fontDefs.values()) {
            loadFontDef(manager, def);
        }
    }

    private static void setupFontResolver(AssetManager manager) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    private static void loadFontDef(AssetManager manager, FontDef def) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter loaderParam = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        loaderParam.fontFileName = def.getFile();

        FreeTypeFontGenerator.FreeTypeFontParameter fontParam = loaderParam.fontParameters;
        fontParam.size = def.getSize();
        fontParam.kerning = def.isKerning();
        fontParam.genMipMaps = def.isGenMipMaps();
        fontParam.minFilter = def.getMinFilter();
        fontParam.magFilter = def.getMagFilter();

        for (LocalizationEntry entry : I18N.getLocalizations()) {
            fontParam.characters += entry.getExtraChars();
        }
        fontParam.characters = uniqueifyCharacters(fontParam.characters);

        manager.load(def.getName(), BitmapFont.class, loaderParam);
    }

    private static String uniqueifyCharacters(String characters) {
        String unique = "";
        for (int i = 0; i < characters.length(); ++i) {
            char c = characters.charAt(i);
            if (!unique.contains(Character.toString(c))) {
                unique += c;
            }
        }
        return unique;
    }
}
