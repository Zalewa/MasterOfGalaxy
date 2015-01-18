package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationChangedListener;
import masterofgalaxy.ecs.components.ColonyComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.ships.ShipDesign;
import masterofgalaxy.ui.ContainerEx;

public class ShipyardProductionUi extends Table implements Localizable {
    private TextButton closeButton;
    private Skin skin;
    private Entity entity;
    private ContainerEx<Table> shipsContainer;

    public Signal<Object> closeRequested = new Signal<Object>();
    public Signal<ShipDesign> designPicked = new Signal<ShipDesign>();

    public ShipyardProductionUi(MogGame game, Skin skin) {
        super(skin);
        this.skin = skin;
        I18N.localeChanged.add(new LocalizationChangedListener(this));

        setupUi();
        applyTranslation();
    }

    private void setupUi() {
        setupCloseButton();
        setupShipsContainer();
    }

    private void setupShipsContainer() {
        shipsContainer = new ContainerEx<Table>();
        shipsContainer.fill();
        add(shipsContainer).expandX().fill();
        row();
    }

    private void setupCloseButton() {
        closeButton = new TextButton("", skin);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                closeRequested.dispatch(null);
            }
        });
        add(closeButton).expandX().fill();
        row();
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        if (this.entity == null) {
            return;
        }

        buildShipDesignsLayout();
    }

    private void buildShipDesignsLayout() {
        Table table = new Table();
        Player owner = Mappers.playerOwner.get(entity).getOwner();
        for (ShipDesign design : owner.getState().getShipDesigns()) {
            addNewShipWidgetToTable(table, design);
        }
        shipsContainer.setActor(table);
    }

    private void addNewShipWidgetToTable(Table table, ShipDesign design) {
        final ShipDesignWidget widget = new ShipDesignWidget(skin, getColonyComponent());
        widget.setShipDesign(design);
        widget.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setShipyardProduction(widget.getShipDesign());
                designPicked.dispatch(widget.getShipDesign());
            }
        });
        table.add(widget).expandX().fillX();
        table.row();
    }

    private void setShipyardProduction(ShipDesign shipDesign) {
        Mappers.colony.get(entity).shipyard.constructedShip = shipDesign;
    }

    private ColonyComponent getColonyComponent() {
        return Mappers.colony.get(entity);
    }

    @Override
    public void applyTranslation() {
        setEntity(entity);
        closeButton.setText(I18N.resolve("$close"));
    }
}
