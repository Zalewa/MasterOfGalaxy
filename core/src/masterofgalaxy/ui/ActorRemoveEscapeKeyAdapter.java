package masterofgalaxy.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorRemoveEscapeKeyAdapter extends EscapeKeyAdapter {
    private Actor actor;
    private boolean enabled = true;

    public ActorRemoveEscapeKeyAdapter(Actor actor) {
        this.actor = actor;
    }

    @Override
    protected boolean escape() {
        if (enabled && actor.getStage().getRoot().getChildren().peek() == actor) {
            actor.remove();
            return true;
        }
        return false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
