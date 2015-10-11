package masterofgalaxy.ui;

import com.badlogic.gdx.Gdx;

public class WindowCloseEscapeKeyAdapter extends EscapeKeyAdapter {
    private WindowExtender actor;
    private boolean enabled = true;

    public WindowCloseEscapeKeyAdapter(WindowExtender actor) {
        this.actor = actor;
    }

    @Override
    protected boolean escape() {
        if (enabled && isLastOpenedWindow()) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    actor.close();
                }
            });
            return true;
        }
        return false;
    }

    private boolean isLastOpenedWindow() {
        return actor.getWindow().getStage().getRoot().getChildren().peek() == actor.getWindow();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
