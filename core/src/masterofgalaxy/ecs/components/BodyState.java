package masterofgalaxy.ecs.components;

import com.badlogic.gdx.math.Vector2;

public class BodyState {
    private Vector2 position = new Vector2();
    private Vector2 size = new Vector2();

    public BodyState() {

    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(float x, float y) {
        this.size.set(x, y);
    }

    public void setSize(Vector2 size) {
        this.size.set(size);
    }

    public void set(BodyState state) {
        setPosition(state.getPosition());
        setSize(state.getSize());
    }
}
