package masterofgalaxy.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import masterofgalaxy.MogGame;
import masterofgalaxy.draw.PrimitiveRectangleDraw;
import masterofgalaxy.ecs.GlobalBody;
import masterofgalaxy.ecs.components.BodyComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.RenderComponent;

public class RenderingSystem extends IteratingSystem {
    private MogGame game;
    private Array<Entity> queue = new Array<Entity>();
    private OrthographicCamera camera = new OrthographicCamera();
    private Viewport viewport = null;
    private boolean drawBounds = false;
    private Vector2 drawedPos = new Vector2();
    private Rectangle drawedBounds = new Rectangle();

    public RenderingSystem(MogGame game) {
        super(Family.getFor(BodyComponent.class, RenderComponent.class));
        this.game = game;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        viewport.apply();
        camera.update();
        SpriteBatch batch = game.getSpriteBatch();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Entity entity : queue) {
            RenderComponent render = Mappers.spriteRender.get(entity);

            if (render.getTexture() == null) {
                continue;
            }

            Vector2 pos = GlobalBody.calculateGlobalPosition(drawedPos, entity);
            float originX = render.getSize().x * 0.5f;
            float originY = render.getSize().y * 0.5f;

            Color oldColor = game.getSpriteBatch().getColor();
            game.getSpriteBatch().setColor(render.getColor());
            game.getSpriteBatch().draw(render.getTexture(), pos.x - originX, pos.y - originY,
                    originX, originY,
                    render.getSize().x, render.getSize().y,
                    render.getScale().x, render.getScale().y,
                    0.0f, 0, 0, (int)render.getSize().x, (int)render.getSize().y,
                    false, false);
            game.getSpriteBatch().setColor(oldColor);

            if (drawBounds) {
                Rectangle bounds = GlobalBody.calculateGlobalBounds(drawedBounds, entity);
                PrimitiveRectangleDraw.drawFrame(game.getSpriteBatch(), Color.WHITE, bounds);
            }
        }
        batch.end();
        queue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        queue.add(entity);
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public boolean isDrawBounds() {
        return drawBounds;
    }

    public void setDrawBounds(boolean drawBounds) {
        this.drawBounds = drawBounds;
    }
}
