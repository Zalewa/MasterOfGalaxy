package masterofgalaxy.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.Sprite;
import masterofgalaxy.ecs.components.*;

public class FleetFactory {
    public static Entity build(MogGame game, PooledEngine engine) {
        Entity entity = engine.createEntity();

        PlayerOwnerComponent owner = engine.createComponent(PlayerOwnerComponent.class);
        entity.add(owner);

        RenderComponent render = engine.createComponent(RenderComponent.class);
        render.setColor(owner.getOwner().getColor());
        render.setTexture(game.getAssetManager().get(Sprite.fleet));
        render.registerColorChangedSignal(owner.ownerColorChanged);
        entity.add(render);

        BodyComponent body = engine.createComponent(BodyComponent.class);
        body.setSize(render.getScaledSize());
        entity.add(body);

        entity.add(engine.createComponent(PickableComponent.class));
        entity.add(engine.createComponent(FleetComponent.class));

        engine.addEntity(entity);
        return entity;
    }
}
