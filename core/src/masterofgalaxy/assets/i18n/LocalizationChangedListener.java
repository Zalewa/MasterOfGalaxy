package masterofgalaxy.assets.i18n;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;

public class LocalizationChangedListener implements Listener<Object> {
    private Localizable localizable;

    public LocalizationChangedListener(Localizable localizable) {
        this.localizable = localizable;
    }

    @Override
    public void receive(Signal<Object> signal, Object object) {
        localizable.applyTranslation();
    }
}
