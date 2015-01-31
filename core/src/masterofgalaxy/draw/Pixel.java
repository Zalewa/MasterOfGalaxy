package masterofgalaxy.draw;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Pixel {
    private static Texture pixel = null;

    public static Texture getPixel() {
        if (pixel == null) {
            pixel = spawn();
        }
        return pixel;
    }

    private static Texture spawn() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }
}
