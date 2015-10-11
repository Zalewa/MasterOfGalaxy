package masterofgalaxy.ui;

import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class WindowExtender {
    public Signal<Object> closedSignal = new Signal<Object>();
    private WindowCloseEscapeKeyAdapter escapeAdapter = null;
    private Window window;
    private Button xButton;
    private boolean destroyOnClose = false;

    public WindowExtender(Window window) {
        this.window = window;
        setupEscapeListener();
        createXButton();

        if (window instanceof Localizable) {
            setupLocalizable((Localizable) window);
        }
    }

    private void setupLocalizable(final Localizable localizable) {
        I18N.addLocalizable(localizable);
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                localizable.applyTranslation();
            }
        });
    }

    private void setupEscapeListener() {
        escapeAdapter = new WindowCloseEscapeKeyAdapter(this);
        window.addListener(escapeAdapter);
    }

    void destroy() {
        if (window instanceof Localizable) {
            I18N.removeLocalizable((Localizable) window);
        }

        closedSignal.dispatch(window);
        closedSignal.removeAllListeners();
        window.clear();
        window.remove();

        closedSignal = null;
        window = null;
        escapeAdapter = null;
    }

    private void createXButton() {
        xButton = new TextButton("X", window.getSkin());
        xButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });
        window.getTitleTable().add(xButton).height(window.getPadTop());
    }

    public Button getXButton() {
        return xButton;
    }

    public void setCloseable(boolean cancelable) {
        escapeAdapter.setEnabled(cancelable);
        xButton.setVisible(cancelable);
    }

    public boolean isDestroyOnClose() {
        return destroyOnClose ;
    }

    public void setDestroyOnClose(boolean destroyOnClose) {
        this.destroyOnClose = destroyOnClose;
    }

    Window getWindow() {
        return window;
    }

    public void close() {
        if (isDestroyOnClose()) {
            destroy();
        } else {
            window.remove();
        }
    }

    public void show(Stage stage) {
        stage.addActor(window);
        stage.setKeyboardFocus(window);
        Ui.centerWithinStage(window);
    }
}
