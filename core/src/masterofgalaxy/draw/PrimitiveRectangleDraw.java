package masterofgalaxy.draw;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PrimitiveRectangleDraw {
    public static void drawFrame(SpriteBatch batch, Color color, Rectangle rectangle) {
        final float thickness = 3.0f;

        Texture pixel = Pixel.getPixel();
        Color oldColor = batch.getColor();
        batch.setColor(color);
        batch.draw(pixel, rectangle.x, rectangle.y, 0, 0,
                1, 1, rectangle.width, thickness,
                0.0f, 0, 0, 1, 1,
                false, false);
        batch.draw(pixel, rectangle.x, rectangle.y, 0, 0,
                1, 1, thickness, rectangle.height,
                0.0f, 0, 0, 1, 1,
                false, false);
        batch.draw(pixel, rectangle.x + rectangle.width, rectangle.y, 0, 0,
                1, 1, thickness, rectangle.height,
                0.0f, 0, 0, 1, 1,
                false, false);
        batch.draw(pixel, rectangle.x, rectangle.y + rectangle.height, 0, 0,
                1, 1, rectangle.width, thickness,
                0.0f, 0, 0, 1, 1,
                false, false);
        batch.setColor(oldColor);
    }

    public static void drawFill(SpriteBatch batch, Color color, Rectangle rectangle) {
        Texture pixel = Pixel.getPixel();
        Color oldColor = batch.getColor();
        batch.setColor(color);
        batch.draw(pixel, rectangle.x, rectangle.y, 0, 0,
                1, 1, rectangle.width, rectangle.height,
                0.0f, 0, 0, 1, 1,
                false, false);
        batch.setColor(oldColor);
    }
}
