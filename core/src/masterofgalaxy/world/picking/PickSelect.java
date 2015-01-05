package masterofgalaxy.world.picking;

import com.badlogic.ashley.core.Entity;

class PickSelect implements PickStrategy {
    @Override
    public void pick(PickLogic pickLogic, Entity picked) {
        pickLogic.setSelection(picked);
    }
}
