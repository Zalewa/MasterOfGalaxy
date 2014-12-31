package masterofgalaxy.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import masterofgalaxy.ecs.components.BlinkComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.RenderComponent;

public class BlinkSystem extends IteratingSystem {
    public BlinkSystem() {
        super(Family.getFor(BlinkComponent.class, RenderComponent.class));
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BlinkComponent blink = Mappers.blink.get(entity);
        if (blink.countdown <= 0.0f) {
            if (!blink.isBlinked) {
                blink.countdown = MathUtils.random(blink.blinkMinInterval, blink.blinkMaxInterval);
            } else {
                blink.countdown = MathUtils.random(blink.blinkedMinInterval, blink.blinkedMaxInterval);
            }
        }
        blink.countdown -= deltaTime;
        if (blink.countdown < 0.0f) {
            blink.isBlinked = !blink.isBlinked;
            RenderComponent render = Mappers.spriteRender.get(entity);
            if (blink.isBlinked) {
                blink.colorUnblinked.set(render.getColor());
                blink.colorBlinked.set(render.getColor());
                blink.colorBlinked.add(blink.magnitude, blink.magnitude, blink.magnitude, 0.0f);
                render.setColor(blink.colorBlinked);
            } else {
                if (render.getColor().equals(blink.colorBlinked)) {
                    render.setColor(blink.colorUnblinked);
                }
            }
        }
    }
}
