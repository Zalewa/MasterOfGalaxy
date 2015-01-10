package masterofgalaxy.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

public class ContainerEx<T extends Actor> extends Container<T> {
    private T actor;

    @Override
    public void setActor(T actor) {
        boolean visible = shouldNewActorBeVisible();
        this.actor = actor;
        if (visible) {
            super.setActor(actor);
        }
    }

    @Override
    public T getActor() {
        return actor;
    }

    public void setActorVisible(boolean b) {
        if (b) {
            super.setActor(actor);
        } else {
            super.setActor(null);
        }
    }

    public boolean isActorVisible() {
        return actor != null && super.getActor() != null;
    }

    private boolean shouldNewActorBeVisible() {
        return actor == null || super.getActor() != null;
    }
}
