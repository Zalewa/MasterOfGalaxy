package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class BodyComponent extends Component implements Pool.Poolable{
    private Vector2 position;
    private Vector2 size;
    private Rectangle bounds = new Rectangle();

    public BodyComponent() {
        position = Vector2.Zero.cpy();
        size = Vector2.Zero.cpy();
    }

    @Override
    public void reset() {
        position.set(Vector2.Zero);
        size.set(Vector2.Zero);
        updateBounds();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        updateBounds();
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
        updateBounds();
    }


    public void translate(float x, float y) {
        position.x += x;
        position.y += y;
        updateBounds();
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(float x, float y) {
        this.size.set(x, y);
        updateBounds();
    }

    public void setSize(Vector2 size) {
        this.size.set(size);
        updateBounds();
    }

    public void scale(float factor) {
        scale(factor, factor);
    }

    public void scale(float x, float y) {
        size.x *= x;
        size.y *= y;
        updateBounds();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private void updateBounds() {
        bounds.width = size.x;
        bounds.height = size.y;
        bounds.x = position.x - size.x / 2.0f;
        bounds.y = position.y - size.y / 2.0f;
    }
}
