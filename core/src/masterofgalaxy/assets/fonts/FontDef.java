package masterofgalaxy.assets.fonts;

import com.badlogic.gdx.graphics.Texture;

public class FontDef {
    private String name;
    private String file;
    private int size;
    private boolean kerning;
    private boolean genMipMaps;
    private Texture.TextureFilter minFilter;
    private Texture.TextureFilter magFilter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isKerning() {
        return kerning;
    }

    public void setKerning(boolean kerning) {
        this.kerning = kerning;
    }

    public boolean isGenMipMaps() {
        return genMipMaps;
    }

    public void setGenMipMaps(boolean genMipMaps) {
        this.genMipMaps = genMipMaps;
    }

    public Texture.TextureFilter getMinFilter() {
        return minFilter;
    }

    public void setMinFilter(Texture.TextureFilter minFilter) {
        this.minFilter = minFilter;
    }

    public Texture.TextureFilter getMagFilter() {
        return magFilter;
    }

    public void setMagFilter(Texture.TextureFilter magFilter) {
        this.magFilter = magFilter;
    }
}
