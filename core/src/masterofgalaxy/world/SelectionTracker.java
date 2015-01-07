package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.utils.Disposable;
import masterofgalaxy.MogGame;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.SelectionComponent;
import masterofgalaxy.ecs.entities.SelectionFactory;

public class SelectionTracker implements Disposable {
    private MogGame game;
    private PooledEngine engine;
    private Entity selection = null;

    public Signal<Entity> selectionChanged = new Signal<Entity>();

    public SelectionTracker(MogGame game, PooledEngine engine) {
        this.game = game;
        this.engine = engine;
    }

    public Entity getSelectedEntity() {
        if (selection != null) {
            SelectionComponent selectionComponent = Mappers.selection.get(selection);
            if (selectionComponent.selection.size > 0) {
                return selectionComponent.selection.first();
            }
        }
        return null;
    }

    public void setSelection(Entity entity) {
        if (entity != null) {
            setSelectionNotNull(entity);
        } else {
            clearSelection();
        }
    }

    private void setSelectionNotNull(Entity entity) {
        if (selection == null) {
            selection = SelectionFactory.build(game, engine);
        }
        SelectionComponent selectionComponent = Mappers.selection.get(selection);
        selectionComponent.selection.clear();
        selectionComponent.selection.add(entity);
        selectionChanged.dispatch(entity);
    }

    public void clearSelection() {
        if (selection != null) {
            engine.removeEntity(selection);
            selection = null;
            selectionChanged.dispatch(null);
        }
    }

    @Override
    public void dispose() {
        selectionChanged.removeAllListeners();
        clearSelection();
    }
}
