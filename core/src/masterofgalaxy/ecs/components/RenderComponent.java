package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class RenderComponent extends Component implements Pool.Poolable {
    private Color color = Color.WHITE.cpy();
    private Texture texture = null;
    private Vector2 scale = new Vector2(1.0f, 1.0f);

    private Vector2 textureSize = new Vector2(0.0f, 0.0f);
    private Vector2 scaledSize = new Vector2(1.0f, 1.0f);

    public RenderComponent() {
    }

    @Override
    public void reset() {
        color.set(Color.WHITE);
        texture = null;
        scale.set(1.0f, 1.0f);
        textureSize.set(0.0f, 0.0f);
    }

    public Vector2 getSize() {
        if (texture != null) {
            textureSize.x = texture.getWidth();
            textureSize.y = texture.getHeight();
        } else {
            textureSize.set(0.0f, 0.0f);
        }
        return textureSize;
    }

    public Vector2 getScaledSize() {
        return scaledSize.set(1.0f, 1.0f).scl(getSize()).scl(getScale());
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public void setColor(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }

    public void setScale(float x, float y) {
        this.scale.set(x, y);
    }

    public void setScaleToSize(float width, float height) {
        setScale(width / getSize().x, height / getSize().y);
    }

    public void setScaleToSize(Vector2 scaleTo) {
        setScale(scaleTo.x / getSize().x, scaleTo.y / getSize().y);
    }

    public void scaleScale(float factor) {
        scale.x *= factor;
        scale.y *= factor;
    }
}
