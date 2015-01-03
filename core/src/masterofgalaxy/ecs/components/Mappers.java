package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Mappers {
    public static final ComponentMapper<BodyComponent> body = ComponentMapper.getFor(BodyComponent.class);
    public static final ComponentMapper<BlinkComponent> blink = ComponentMapper.getFor(BlinkComponent.class);
    public static final ComponentMapper<RenderComponent> spriteRender = ComponentMapper.getFor(RenderComponent.class);
    public static final ComponentMapper<NameComponent> name = ComponentMapper.getFor(NameComponent.class);
    public static final ComponentMapper<PickableComponent> pickable = ComponentMapper.getFor(PickableComponent.class);
    public static final ComponentMapper<SelectionComponent> selection = ComponentMapper.getFor(SelectionComponent.class);
    public static final ComponentMapper<StarComponent> star = ComponentMapper.getFor(StarComponent.class);
    public static final ComponentMapper<ParentshipComponent> parentship = ComponentMapper.getFor(ParentshipComponent.class);
    public static final ComponentMapper<TextRenderComponent> textRender = ComponentMapper.getFor(TextRenderComponent.class);
    public static final ComponentMapper<PlayerOwnerComponent> playerOwner = ComponentMapper.getFor(PlayerOwnerComponent.class);
}
