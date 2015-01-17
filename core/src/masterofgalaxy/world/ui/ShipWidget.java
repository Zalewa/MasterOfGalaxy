package masterofgalaxy.world.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import masterofgalaxy.ecs.components.FleetComponent;

public class ShipWidget extends Table {
    private Label nameLabel;
    private Label countLabel;
    private Skin skin;

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
    }

    public void setShip(FleetComponent.Ship ship) {
        nameLabel.setText(ship.design.getName());
        countLabel.setText("" + ship.count);
    }
}
