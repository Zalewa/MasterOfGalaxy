package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class BodyComponent extends Component implements Pool.Poolable{
    private final BodyState state = new BodyState();
    private Rectangle bounds = new Rectangle();

    public BodyComponent() {
        state.setPosition(Vector2.Zero.cpy());
        state.setSize(Vector2.Zero.cpy());
    }

    @Override
    public void reset() {
        state.setPosition(Vector2.Zero);
        state.setSize(Vector2.Zero);
        updateBounds();
    }

    public Vector2 getPosition() {
        return state.getPosition();
    }

    public void setPosition(float x, float y) {
        state.setPosition(x, y);
        updateBounds();
    }

    public void setPosition(Vector2 position) {
        state.setPosition(position);
        updateBounds();
    }

    public void translate(float x, float y) {
        state.getPosition().x += x;
        state.getPosition().y += y;
        updateBounds();
    }

    public Vector2 getSize() {
        return state.getSize();
    }

    public void setSize(float x, float y) {
        state.setSize(x, y);
    }

    public void setSize(Vector2 size) {
        state.setSize(size);
    }

    public void scale(float factor) {
        scale(factor, factor);
    }

    public void scale(float x, float y) {
        state.getSize().x *= x;
        state.getSize().y *= y;
        updateBounds();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private void updateBounds() {
        bounds.width = state.getSize().x;
        bounds.height = state.getSize().y;
        bounds.x = state.getPosition().x - state.getSize().x / 2.0f;
        bounds.y = state.getPosition().y - state.getSize().y / 2.0f;
    }

    public BodyState getState() {
        return state;
    }

    public void setState(BodyState state) {
        this.state.set(state);
        updateBounds();
    }
}
