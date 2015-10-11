package masterofgalaxy.world.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import masterofgalaxy.ecs.components.FleetComponent;

public class ShipWidget extends Table {
    private Label nameLabel;
    private Label countLabel;
    private FleetComponent.Ship ship;

    public ShipWidget(Skin skin) {
        super(skin);
        setupUi();
    }

    private void setupUi() {
        nameLabel = new Label("", getSkin());
        add(nameLabel).expandX().left();

        countLabel = new Label("", getSkin());
        add(countLabel).expandX().right();
        row();
    }

    public void setShip(FleetComponent.Ship ship) {
        this.ship = ship;

        nameLabel.setText(ship.design.getName());
        countLabel.setText("" + ship.count);
    }

    public FleetComponent.Ship getShip() {
        return ship;
    }

    public int getSelectedAmount() {
        return ship.count;
    }
}
