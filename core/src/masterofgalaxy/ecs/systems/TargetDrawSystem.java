package masterofgalaxy.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import masterofgalaxy.MogGame;
import masterofgalaxy.ecs.GlobalBody;
import masterofgalaxy.ecs.components.BodyComponent;
import masterofgalaxy.ecs.components.EntityTargetComponent;
import masterofgalaxy.ecs.components.Mappers;

public class TargetDrawSystem extends IteratingSystem {
    private static final int segments = 15;
    private static final float segmentOffsetSpeed = 1.0f;

    private MogGame game;
    private Array<Entity> queue = new Array<Entity>();
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;
    private Vector2 drawedStartPos = new Vector2();
    private Vector2 drawedEndPos = new Vector2();
    private Vector2 drawedIntermediateStartPos = new Vector2();
    private Vector2 drawedIntermediateEndPos = new Vector2();
    private Vector2 segment = new Vector2();
    private Vector2 segmentShift = new Vector2();
    private Color colorStart = new Color(0.0f, 0.5f, 0.0f, 1.0f);
    private Color colorEnd = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    private float segmentOffsetTime = 0.0f;

    public TargetDrawSystem(MogGame game) {
        super(Family.getFor(EntityTargetComponent.class, BodyComponent.class));
        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        queue.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        segmentOffsetTime += deltaTime;
        while (segmentOffsetTime > segmentOffsetSpeed) {
            segmentOffsetTime -= segmentOffsetSpeed;
        }

        viewport.apply();
        camera.update();
        renderer = game.getShapeRenderer();

        Gdx.gl20.glLineWidth(2.0f);
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        Color oldColor = renderer.getColor();
        for (Entity entity : queue) {
            Entity target = Mappers.entityTarget.get(entity).target;
            if (target != null) {
                GlobalBody.calculateGlobalPosition(drawedStartPos, entity);
                GlobalBody.calculateGlobalPosition(drawedEndPos, target);

                drawedIntermediateStartPos.set(drawedStartPos);

                for (int segment = -1; segment < segments; ++segment) {
                    drawSegment(segment);
                }
            }
        }
        renderer.setColor(oldColor);
        renderer.end();
        Gdx.gl20.glLineWidth(1.0f);
        queue.clear();
    }

    private void drawSegment(int segmentNr) {
        segmentShift.set(drawedEndPos).sub(drawedStartPos).scl(1.0f / segments).scl(getSegmentOffset());
        if (segmentNr < 0) {
            drawedIntermediateStartPos.set(drawedStartPos);
        } else {
            drawedIntermediateStartPos.set(drawedEndPos).sub(drawedStartPos).scl((segmentNr) / (float)segments).add(drawedStartPos);
            drawedIntermediateStartPos.add(segmentShift);
        }

        if (segmentNr >= segments - 1) {
            drawedIntermediateEndPos.set(drawedEndPos);
        } else {
            drawedIntermediateEndPos.set(drawedEndPos).sub(drawedStartPos).scl((segmentNr + 1.0f) / (float)segments).add(drawedStartPos);
            drawedIntermediateEndPos.add(segmentShift);
        }

        renderer.line(drawedIntermediateStartPos.x, drawedIntermediateStartPos.y,
                drawedIntermediateEndPos.x, drawedIntermediateEndPos.y, colorStart, colorEnd);
    }

    private float getSegmentOffset() {
        return segmentOffsetTime / segmentOffsetSpeed;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
}
