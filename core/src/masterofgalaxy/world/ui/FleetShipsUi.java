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
import masterofgalaxy.world.FleetSplitter;

public class FleetShipsUi extends Table implements Localizable {
    private Entity entity;
    private Container<Table> shipWidgetsLayout;
    private Array<ShipWidget> shipWidgets = new Array<ShipWidget>();
    private Skin skin;

    public FleetShipsUi(Skin skin) {
        super(skin);
        this.skin = skin;
        I18N.localeChanged.add(new LocalizationChangedListener(this));

        pad(5.0f);

        setupUi();
        applyTranslation();
    }

    private void setupUi() {
        shipWidgetsLayout = new Container<Table>();
        add(shipWidgetsLayout).expandX().fillX();
    }

    public void setEntity(Entity entity) {
        shipWidgets.clear();
        this.entity = entity;
        if (entity == null) {
            return;
        }

        Table table = new Table();
        shipWidgetsLayout.setActor(table);
        shipWidgetsLayout.fillX();
        FleetComponent fleetComponent = Mappers.fleet.get(entity);
        for (FleetComponent.Ship ship : fleetComponent.ships) {
            if (ship.count > 0) {
                ShipWidget shipWidget = new ShipWidget(skin);
                shipWidget.setShip(ship);
                table.add(shipWidget).expandX().fillX();
                table.row();
                shipWidgets.add(shipWidget);
            }
        }
    }

    public void fillInSplitter(FleetSplitter splitter) {
        for (ShipWidget widget : shipWidgets) {
            splitter.setShipAmount(widget.getShip().design, widget.getSelectedAmount());
        }
    }

    @Override
    public void applyTranslation() {
        setEntity(entity);
    }
}
