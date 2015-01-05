package masterofgalaxy.world.picking;

import com.badlogic.ashley.core.Entity;

interface PickStrategy {
    public void pick(PickLogic pickLogic, Entity picked);
}
