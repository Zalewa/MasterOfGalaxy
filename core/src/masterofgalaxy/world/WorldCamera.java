package masterofgalaxy.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class WorldCamera {
    private Rectangle viewField;
    private OrthographicCamera camera = new OrthographicCamera();

    public WorldCamera() {
    }

    public void setViewField(Rectangle viewField) {
        this.viewField = viewField;
    }

    public void centerCamera() {
        camera.position.x = viewField.x + viewField.getWidth() * 0.5f;
        camera.position.y = viewField.y + viewField.getHeight() * 0.5f;
    }

    public void lockCamera() {
        float effectiveViewWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewHeight = camera.viewportHeight * camera.zoom;

        float halfWidth = effectiveViewWidth * 0.5f;
        float halfHeight = effectiveViewHeight * 0.5f;

        if (effectiveViewWidth > viewField.width) {
            camera.position.x = viewField.x + viewField.width * 0.5f;
        } else {
            if (camera.position.x - halfWidth < viewField.x) {
                camera.position.x = viewField.x + halfWidth;
            } else if (camera.position.x + halfWidth > viewField.x + viewField.width) {
                camera.position.x = viewField.x + viewField.width - halfWidth;
            }
        }

        if (effectiveViewHeight > viewField.height) {
            camera.position.y = viewField.y + viewField.height * 0.5f;
        } else {
            if (camera.position.y - halfHeight < viewField.y) {
                camera.position.y = viewField.y + halfHeight;
            } else if (camera.position.y + halfHeight > viewField.y + viewField.height) {
                camera.position.y = viewField.y + viewField.height - halfHeight;
            }
        }
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public float getZoom() {
        return camera.zoom;
    }

    public void translate(float x, float y) {
        camera.translate(x, y);
        lockCamera();
    }

    public void zoomBy(float delta) {
        camera.zoom += delta;
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, getMaxZoom());
        lockCamera();
    }

    public float getMaxZoom() {
        return 1.0f;
    }

    public void resetZoom() {
        camera.zoom = getMaxZoom();
    }
}
