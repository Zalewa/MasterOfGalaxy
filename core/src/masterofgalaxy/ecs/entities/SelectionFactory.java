package masterofgalaxy.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.Sprite;
import masterofgalaxy.ecs.components.BodyComponent;
import masterofgalaxy.ecs.components.NameComponent;
import masterofgalaxy.ecs.components.RenderComponent;
import masterofgalaxy.ecs.components.SelectionComponent;

public class SelectionFactory {
    public static Entity build(MogGame game, PooledEngine entityEngine) {
        Entity entity = entityEngine.createEntity();

        RenderComponent render = entityEngine.createComponent(RenderComponent.class);
        render.setTexture(game.getAssetManager().get(Sprite.selection));
        render.setColor(Color.LIGHT_GRAY);
        entity.add(render);

        BodyComponent body = entityEngine.createComponent(BodyComponent.class);
        body.setSize(render.getScaledSize().x, render.getScaledSize().y);
        entity.add(body);

        entity.add(entityEngine.createComponent(SelectionComponent.class));
        NameComponent name = entityEngine.createComponent(NameComponent.class);
        name.setName("selection");
        entity.add(name);

        entityEngine.addEntity(entity);

        return entity;
    }
}
