package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import masterofgalaxy.world.stars.PlanetClass;
import masterofgalaxy.world.stars.StarClass;

public class StarComponent extends Component implements Pool.Poolable {
    public StarClass klass = StarClass.nullKlass;
    public PlanetClass planetKlass = PlanetClass.nullKlass;

    @Override
    public void reset() {
        klass = StarClass.nullKlass;
        planetKlass = PlanetClass.nullKlass;
    }

    public void setState(StarComponent star) {
        klass = star.klass;
        planetKlass = star.planetKlass;
    }
}
