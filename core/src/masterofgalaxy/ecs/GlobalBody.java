package masterofgalaxy.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import masterofgalaxy.ecs.components.BodyComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.ParentshipComponent;

public class GlobalBody {
    public static Vector2 calculateGlobalPosition(Vector2 out, Entity entity) {
        BodyComponent body = Mappers.body.get(entity);
        out.set(body.getPosition());
        ParentshipComponent parentship = Mappers.parentship.get(entity);
        while (parentship != null && parentship.getParent() != null) {
            body = Mappers.body.get(parentship.getParent());
            out.add(body.getPosition());
            parentship = Mappers.parentship.get(parentship.getParent());
        }
        return out;
    }

    public static Rectangle calculateGlobalBounds(Rectangle out, Entity entity) {
        BodyComponent body = Mappers.body.get(entity);
        out.set(body.getBounds());
        ParentshipComponent parentship = Mappers.parentship.get(entity);
        while (parentship != null && parentship.getParent() != null) {
            body = Mappers.body.get(parentship.getParent());
            out.x += body.getPosition().x;
            out.y += body.getPosition().y;
            parentship = Mappers.parentship.get(parentship.getParent());
        }
        return out;
    }
}
