package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.MogGame;
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
    private MogGame game;

    public FleetShipsUi(MogGame game, Skin skin) {
        super(skin);
        this.game = game;
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
                ShipWidget shipWidget = mkShipWidget();
                shipWidget.setShip(ship);
                table.add(shipWidget).expandX().fillX();
                table.row();
                shipWidgets.add(shipWidget);
            }
        }
    }

    private ShipWidget mkShipWidget() {
        if (isOwnedByCurrentPlayer()) {
            return new ShipSplittableWidget(getSkin());
        } else {
            return new ShipWidget(getSkin());
        }
    }

    private boolean isOwnedByCurrentPlayer() {
        return Mappers.playerOwner.get(entity).getOwner() == game.getWorldScreen().getCurrentPlayer();
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
