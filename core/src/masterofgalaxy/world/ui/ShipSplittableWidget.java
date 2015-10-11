package masterofgalaxy.world.ui;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import masterofgalaxy.ecs.components.FleetComponent;

public class ShipSplittableWidget extends ShipWidget {
    private Slider amountSlider;
    private Label selectedAmountLabel;

    public ShipSplittableWidget(Skin skin) {
        super(skin);
        setupUi();
    }

    private void setupUi() {
        amountSlider = new Slider(0.0f, 0.0f, 1.0f, false, getSkin());
        amountSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedAmountLabel.setText("" + getSelectedAmount());
            }
        });
        add(amountSlider).expandX().center();

        selectedAmountLabel = new Label("", getSkin());
        add(selectedAmountLabel).right();
    }

    @Override
    public void setShip(FleetComponent.Ship ship) {
        super.setShip(ship);

        amountSlider.setRange(0.0f, ship.count);
        amountSlider.setValue(ship.count);
    }

    @Override
    public int getSelectedAmount() {
        return MathUtils.round(amountSlider.getValue());
    }
}
