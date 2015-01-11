package masterofgalaxy.world.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ResourceDistributionSlider {
    private Label label;
    private Slider slider;
    private Label valueLabel;
    private boolean locked = false;
    private Skin skin;

    public ResourceDistributionSlider(Skin skin) {
        this.label = new Label("", skin);
        label.setTouchable(Touchable.enabled);
        this.valueLabel = new Label("", skin);
        this.slider = new Slider(0.0f, 1.0f, 0.01f, false, skin);
        this.skin = skin;
        updateLockedStyle();
    }

    public Label getLabel() {
        return label;
    }

    public Label getValueLabel() {
        return valueLabel;
    }

    public Slider getSlider() {
        return slider;
    }

    public void setLabelText(String text) {
        label.setText(text);
    }

    public void addLabelClickListener(ClickListener listener) {
        label.addListener(listener);
    }

    public void addValueChangedListener(ChangeListener listener) {
        slider.addListener(listener);
    }

    public float getSliderValue() {
        return slider.getValue();
    }

    public void setSliderValue(float value) {
        slider.setValue(value);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        updateLockedStyle();
    }

    private void updateLockedStyle() {
        String styleName = locked ? "resource-locked" : "resource-unlocked";
        label.setStyle(skin.get(styleName, Label.LabelStyle.class));
    }

    public boolean isActorMine(Actor actor) {
        return actor == label || actor == slider || actor == valueLabel;
    }
}
