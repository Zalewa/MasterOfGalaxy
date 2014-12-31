package masterofgalaxy.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import jdk.nashorn.internal.objects.Global;
import masterofgalaxy.ecs.components.BodyComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.NameComponent;
import masterofgalaxy.ecs.components.PickableComponent;

public class EntityPicker {
    private Engine engine;

    public EntityPicker(Engine engine) {
        this.engine = engine;
    }

    public Entity pickFirstEntity(float x, float y) {
        return pickNextEntity(null, x, y);
    }

    public Entity pickNextEntity(Entity current, float x, float y) {
        ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.getFor(BodyComponent.class, PickableComponent.class));
        Entity first = null;
        Entity candidate = null;
        boolean currentFound = false;
        Rectangle bounds = new Rectangle();
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            bounds = GlobalBody.calculateGlobalBounds(bounds, entity);
            if (bounds.contains(x, y)) {
                if (current == null) {
                    return entity;
                }
                if (first == null) {
                    first = entity;
                }
                if (!currentFound) {
                    if (entity == current) {
                        currentFound = true;
                    }
                } else {
                    return entity;
                }
            }
        }
        return first;
    }

    public Array<Entity> pickEntities(float x, float y) {
        Rectangle bounds = new Rectangle();
        Array<Entity> result = new Array<Entity>();
        ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.getFor(BodyComponent.class, PickableComponent.class));
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            bounds = GlobalBody.calculateGlobalBounds(bounds, entity);
            if (bounds.contains(x, y)) {
                result.add(entity);
            }
        }
        return result;
    }
}
