package masterofgalaxy.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import masterofgalaxy.assets.Background;

public class WorldBackground {
    private ParallaxCamera camera;
    private WorldScreen screen;
    private Texture texture;

    public WorldBackground(WorldScreen screen) {
        this.screen = screen;
        camera = new ParallaxCamera(0.0f, 0.0f);
        texture = screen.getGame().getAssetManager().get(Background.seamlessSpace);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    public void render(float delta) {
        final float drawResizeFactor = 4.0f;

        SpriteBatch batch = screen.getGame().getSpriteBatch();
        Viewport viewport = screen.getViewport();
        viewport.apply();

        camera.viewportWidth = viewport.getWorldWidth();
        camera.viewportHeight = viewport.getWorldHeight();
        camera.zoom = 1.0f - 0.1f * (screen.getCamera().getMaxZoom() - screen.getCamera().getZoom());
        camera.position.set(screen.getCamera().getCamera().position);

        batch.setProjectionMatrix(camera.calculateParallaxMatrix(0.1f, 0.1f));
        batch.begin();

        float drawWidth = viewport.getWorldWidth() * drawResizeFactor;
        float drawHeight = viewport.getWorldHeight() * drawResizeFactor;

        float dim = 0.75f;
        batch.setColor(dim, dim, dim, 1.0f);
        batch.draw(texture,
                -drawWidth * 0.5f, -drawHeight * 0.5f,
                drawWidth, drawHeight,
                0, drawHeight / texture.getHeight(),
                drawWidth / texture.getWidth(), 0);

        batch.end();
    }

    class ParallaxCamera extends OrthographicCamera {
        Matrix4 parallaxView = new Matrix4();
        Matrix4 parallaxCombined = new Matrix4();
        Vector3 tmp = new Vector3();
        Vector3 tmp2 = new Vector3();

        public ParallaxCamera (float viewportWidth, float viewportHeight) {
            super(viewportWidth, viewportHeight);
        }

        public Matrix4 calculateParallaxMatrix (float parallaxX, float parallaxY) {
            update();
            tmp.set(position);
            tmp.x *= parallaxX;
            tmp.y *= parallaxY;

            parallaxView.setToLookAt(tmp, tmp2.set(tmp).add(direction), up);
            parallaxCombined.set(projection);
            Matrix4.mul(parallaxCombined.val, parallaxView.val);
            return parallaxCombined;
        }
    }
}
