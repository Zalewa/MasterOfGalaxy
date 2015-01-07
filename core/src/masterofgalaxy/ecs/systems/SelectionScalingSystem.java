package masterofgalaxy.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import masterofgalaxy.ecs.GlobalBody;
import masterofgalaxy.ecs.components.BodyComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.RenderComponent;
import masterofgalaxy.ecs.components.SelectionComponent;

public class SelectionScalingSystem extends IteratingSystem {
    private Rectangle workRect = new Rectangle();

    public SelectionScalingSystem() {
        super(Family.getFor(SelectionComponent.class, BodyComponent.class, RenderComponent.class));
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent body = Mappers.body.get(entity);
        RenderComponent render = Mappers.spriteRender.get(entity);
        SelectionComponent selection = Mappers.selection.get(entity);

        if (selection.selection.size > 0) {
            Rectangle selectedBounds = GlobalBody.calculateGlobalBounds(workRect, selection.selection.first());
            float x1 = selectedBounds.x;
            float x2 = selectedBounds.x + selectedBounds.width;
            float y1 = selectedBounds.y;
            float y2 = selectedBounds.y + selectedBounds.height;

            for (Entity selected : selection.selection) {
                selectedBounds = GlobalBody.calculateGlobalBounds(workRect, selection.selection.first());

                x1 = Math.min(x1, selectedBounds.x);
                x2 = Math.max(x2, selectedBounds.x + selectedBounds.width);
                y1 = Math.min(y1, selectedBounds.y);
                y2 = Math.max(y2, selectedBounds.y + selectedBounds.height);
            }
            body.setPosition(x1 + (x2 - x1) * 0.5f, y1 + (y2 - y1) * 0.5f);
            body.setSize(x2 - x1, y2 - y1);
            render.setScaleToSize(x2 - x1, y2 - y1);
        }
    }
}
