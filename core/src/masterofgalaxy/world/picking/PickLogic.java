package masterofgalaxy.world.picking;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import masterofgalaxy.world.SelectionTracker;
import masterofgalaxy.world.WorldScreen;

public class PickLogic implements MediaDisposer.Disposable {
    private SelectionTracker selection;
    private WorldScreen screen;

    public PickLogic(WorldScreen screen) {
        this.selection = new SelectionTracker(screen.getGame(), screen.getEntityEngine());
        this.screen = screen;
    }

    @Override
    public void dispose() {
        selection.dispose();
    }

    public void addSelectionChangedListener(Listener<Entity> selectionChangedListener) {
        selection.selectionChanged.add(selectionChangedListener);
    }

    public void pick(Entity entity) {
        PickStrategyFactory.build(this, entity).pick(this, entity);
    }

    public Entity getSelectedEntity() {
        return selection.getSelectedEntity();
    }

    public void setSelection(Entity target) {
        selection.setSelection(target);
    }

    public WorldScreen getScreen() {
        return screen;
    }
}
