package masterofgalaxy.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class ActorRemoveEscapeKeyAdapter extends EscapeKeyAdapter {
    private Actor actor;

    public ActorRemoveEscapeKeyAdapter(Actor actor) {
        this.actor = actor;
    }

    @Override
    protected boolean escape() {
        if (actor.getStage().getRoot().getChildren().peek() == actor) {
            actor.remove();
            return true;
        }
        return false;
    }
}
