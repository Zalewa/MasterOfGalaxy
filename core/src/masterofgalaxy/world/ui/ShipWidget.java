package masterofgalaxy.world.ui;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import masterofgalaxy.ecs.components.FleetComponent;

public class ShipWidget extends Table {
    private Label nameLabel;
    private Label countLabel;
    private Slider amountSlider;
    private Label selectedAmountLabel;
    private Skin skin;
    private FleetComponent.Ship ship;

    public ShipWidget(Skin skin) {
        super(skin);
        this.skin = skin;
        setupUi();
    }

    private void setupUi() {
        nameLabel = new Label("", skin);
        add(nameLabel).expandX().left();

        countLabel = new Label("", skin);
        add(countLabel).expandX().right();
        row();

        amountSlider = new Slider(0.0f, 0.0f, 1.0f, false, skin);
        amountSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedAmountLabel.setText("" + getSelectedAmount());
            }
        });
        add(amountSlider).expandX().center();

        selectedAmountLabel = new Label("", skin);
        add(selectedAmountLabel).right();
    }

    public void setShip(FleetComponent.Ship ship) {
        this.ship = ship;

        nameLabel.setText(ship.design.getName());
        countLabel.setText("" + ship.count);

        amountSlider.setRange(0.0f, (float) ship.count);
        amountSlider.setValue((float) ship.count);
    }

    public FleetComponent.Ship getShip() {
        return ship;
    }

    public int getSelectedAmount() {
        return MathUtils.round(amountSlider.getValue());
    }
}
