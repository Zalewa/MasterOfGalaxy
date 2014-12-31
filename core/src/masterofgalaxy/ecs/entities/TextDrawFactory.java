package masterofgalaxy.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.Font;
import masterofgalaxy.ecs.components.BodyComponent;
import masterofgalaxy.ecs.components.ParentshipComponent;
import masterofgalaxy.ecs.components.TextRenderComponent;

public class TextDrawFactory {
    public static Entity build(MogGame game, PooledEngine entityEngine) {
        Entity entity = entityEngine.createEntity();
        entity.add(entityEngine.createComponent(BodyComponent.class));

        TextRenderComponent textRender = entityEngine.createComponent(TextRenderComponent.class);
        textRender.font = game.getAssetManager().get(Font.starNameFont);
        entity.add(textRender);
        entity.add(entityEngine.createComponent(ParentshipComponent.class));
        return entity;
    }
}
