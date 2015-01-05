package masterofgalaxy.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class WorldCamera {
    private WorldScreen worldScreen;
    private OrthographicCamera camera = new OrthographicCamera();

    public WorldCamera(WorldScreen worldScreen) {
        this.worldScreen = worldScreen;
    }

    public void centerCamera() {
        camera.position.x = worldScreen.getWorld().getPlayField().getWidth() * 0.5f;
        camera.position.y = worldScreen.getWorld().getPlayField().getHeight() * 0.5f;
    }

    public void lockCamera() {
        float effectiveViewWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewHeight = camera.viewportHeight * camera.zoom;

        float halfWidth = effectiveViewWidth * 0.5f;
        float halfHeight = effectiveViewHeight * 0.5f;

        Rectangle field = worldScreen.getWorld().getPlayField();

        if (effectiveViewWidth > field.width) {
            camera.position.x = field.x + field.width * 0.5f;
        } else {
            if (camera.position.x - halfWidth < field.x) {
                camera.position.x = field.x + halfWidth;
            } else if (camera.position.x + halfWidth > field.x + field.width) {
                camera.position.x = field.x + field.width - halfWidth;
            }
        }

        if (effectiveViewHeight > field.height) {
            camera.position.y = field.y + field.height * 0.5f;
        } else {
            if (camera.position.y - halfHeight < field.y) {
                camera.position.y = field.y + halfHeight;
            } else if (camera.position.y + halfHeight > field.y + field.height) {
                camera.position.y = field.y + field.height - halfHeight;
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
