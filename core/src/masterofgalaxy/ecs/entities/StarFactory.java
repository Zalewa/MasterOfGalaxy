package masterofgalaxy.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.graphics.Color;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.Sprite;
import masterofgalaxy.ecs.components.*;

public class StarFactory {
    public static Entity build(MogGame game, PooledEngine entityEngine) {
        Entity entity = entityEngine.createEntity();

        RenderComponent render = entityEngine.createComponent(RenderComponent.class);
        render.setTexture(game.getAssetManager().get(Sprite.star));
        render.setScale(0.5f, 0.5f);
        render.setColor(Color.LIGHT_GRAY);
        entity.add(render);

        BodyComponent body = entityEngine.createComponent(BodyComponent.class);
        body.setSize(render.getScaledSize().x, render.getScaledSize().y);
        entity.add(body);

        entity.add(entityEngine.createComponent(NameComponent.class));
        entity.add(entityEngine.createComponent(PickableComponent.class));
        entity.add(entityEngine.createComponent(BlinkComponent.class));
        entity.add(entityEngine.createComponent(StarComponent.class));
        entity.add(entityEngine.createComponent(ParentshipComponent.class));
        entity.add(entityEngine.createComponent(PlayerOwnerComponent.class));

        entityEngine.addEntity(entity);

        return entity;
    }

    public static Entity buildNameDrawable(MogGame game, PooledEngine entityEngine, Entity star) {
        NameComponent starName = Mappers.name.get(star);
        BodyComponent starBody = Mappers.body.get(star);
        PlayerOwnerComponent starOwner = Mappers.playerOwner.get(star);
        ParentshipComponent starParentship = Mappers.parentship.get(star);

        Entity drawable = TextDrawFactory.build(game, entityEngine);

        TextRenderComponent render = Mappers.textRender.get(drawable);
        render.registerTextChangedSignal(starName.nameChanged);
        render.registerColorChangedSignal(starOwner.ownerColorChanged);
        render.tint.set(starOwner.getOwner().getColor());

        BodyComponent body = Mappers.body.get(drawable);
        body.setPosition(0.0f, -starBody.getSize().y * 0.5f + 5.0f);

        starParentship.linkChild(star, drawable);

        entityEngine.addEntity(drawable);

        return drawable;
    }
}
