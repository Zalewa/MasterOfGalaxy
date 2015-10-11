package masterofgalaxy.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import masterofgalaxy.MogGame;
import masterofgalaxy.ecs.GlobalBody;
import masterofgalaxy.ecs.components.BodyComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.TextRenderComponent;

public class TextRenderingSystem extends IteratingSystem {
    private MogGame game;
    private Array<Entity> queue = new Array<Entity>();
    private Camera uiCamera;
    private Viewport gameViewport;
    private Viewport uiViewport;
    private Vector3 projectedPos = new Vector3();
    private Vector2 drawedPos = new Vector2();
    private GlyphLayout glyphLayout = new GlyphLayout();

    public TextRenderingSystem(MogGame game) {
        super(Family.getFor(BodyComponent.class, TextRenderComponent.class));
        this.game = game;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        uiViewport.apply();
        uiCamera.update();
        SpriteBatch batch = game.getSpriteBatch();

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        for (Entity entity : queue) {
            TextRenderComponent render = Mappers.textRender.get(entity);

            if (render.font == null) {
                continue;
            }

            Vector2 pos = GlobalBody.calculateGlobalPosition(drawedPos, entity);
            projectedPos.set(pos.x, pos.y, 0.0f);
            projectedPos = gameViewport.project(projectedPos);

            glyphLayout.setText(render.font, render.text);
            float originX = glyphLayout.width * 0.5f;
            float originY = glyphLayout.height * 0.5f;

            render.font.setColor(render.tint);
            render.font.draw(game.getSpriteBatch(), render.text, projectedPos.x - originX, projectedPos.y - originY);
        }
        batch.end();
        queue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        queue.add(entity);
    }

    public void setGameViewport(Viewport gameViewport) {
        this.gameViewport = gameViewport;
    }

    public void setUiCamera(Camera camera) {
        this.uiCamera = camera;
    }

    public void setUiViewport(Viewport viewport) {
        this.uiViewport = viewport;
    }

}
