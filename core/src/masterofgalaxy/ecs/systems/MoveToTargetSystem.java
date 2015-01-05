package masterofgalaxy.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import masterofgalaxy.ecs.components.BodyComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.MoveTargetComponent;

public class MoveToTargetSystem extends IteratingSystem {
    private Vector2 diff = new Vector2();
    private Vector2 step = new Vector2();
    private int numProcessed = 0;

    public MoveToTargetSystem() {
        super(Family.getFor(BodyComponent.class, MoveTargetComponent.class));
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent body = Mappers.body.get(entity);
        MoveTargetComponent moveTarget = Mappers.moveTarget.get(entity);

        if (!body.getPosition().epsilonEquals(moveTarget.getTarget(), 0.001f) || moveTarget.isFirstStep()) {
            moveTarget.clearFirstStep();
            diff.set(moveTarget.getTarget()).sub(body.getPosition());
            step.set(diff).nor().scl(moveTarget.speed * deltaTime);

            if (step.len2() < diff.len2()) {
                body.translate(step.x, step.y);
            } else {
                moveTarget.destinationReached.dispatch(entity);
                body.setPosition(moveTarget.getTarget());
            }
            ++numProcessed;
        }
    }

    @Override
    public void update(float deltaTime) {
        numProcessed = 0;
        super.update(deltaTime);
    }

    public int getNumProcessed() {
        return numProcessed;
    }
}
