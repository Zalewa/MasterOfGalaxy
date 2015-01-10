package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import masterofgalaxy.world.stars.Planet;
import masterofgalaxy.world.stars.StarClass;

public class StarComponent extends Component implements Pool.Poolable {
    public StarClass klass = StarClass.nullKlass;
    public Planet planet = Planet.nullPlanet;

    @Override
    public void reset() {
        klass = StarClass.nullKlass;
        planet = Planet.nullPlanet;
    }

    public void setState(StarComponent star) {
        klass = star.klass;
        planet = star.planet;
    }
}
