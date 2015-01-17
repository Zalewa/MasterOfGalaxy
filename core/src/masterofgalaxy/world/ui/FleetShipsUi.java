package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationChangedListener;
import masterofgalaxy.ecs.components.FleetComponent;
import masterofgalaxy.ecs.components.Mappers;

public class FleetShipsUi extends Table implements Localizable {
    private Entity entity;
    private Container<Table> shipWidgets;
    private Skin skin;

    public FleetShipsUi(Skin skin) {
        super(skin);
        this.skin = skin;
        I18N.localeChanged.add(new LocalizationChangedListener(this));

        pad(5.0f);

        setupUi();
    }

    private void setupUi() {
        shipWidgets = new Container<Table>();
        add(shipWidgets).expandX().fillX();
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        if (entity == null) {
            return;
        }

        Table table = new Table();
        shipWidgets.setActor(table);
        shipWidgets.fillX();
        FleetComponent fleetComponent = Mappers.fleet.get(entity);
        for (FleetComponent.Ship ship : fleetComponent.ships) {
            ShipWidget shipWidget = new ShipWidget(skin);
            shipWidget.setShip(ship);
            table.add(shipWidget).expandX().fillX();
            table.row();
        }
    }

    @Override
    public void applyTranslation() {
        setEntity(entity);
    }
}
