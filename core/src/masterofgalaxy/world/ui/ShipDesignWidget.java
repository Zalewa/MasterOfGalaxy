package masterofgalaxy.world.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.ecs.components.ColonyComponent;
import masterofgalaxy.gamestate.ships.ShipDesign;

public class ShipDesignWidget extends Table {
    private Label nameLabel;
    private Label productionPerTurnLabel;
    private ColonyComponent colony;
    private ShipDesign shipDesign;

    public ShipDesignWidget(Skin skin, ColonyComponent colony) {
        super(skin);
        this.colony = colony;

        setupUi();
    }

    private void setupUi() {
        setupNameLabel();
        setupProductionPerTurnLabel();
        row();
    }

    private void setupNameLabel() {
        nameLabel = new Label("", getSkin());
        add(nameLabel).expandX().left();
    }

    private void setupProductionPerTurnLabel() {
        productionPerTurnLabel = new Label("", getSkin());
        add(productionPerTurnLabel).expandX().right();
    }

    public void setShipDesign(ShipDesign design) {
        shipDesign = design;
        nameLabel.setText(design.getName());
        float productionPerTurn = colony.getShipProduction() / design.getCost();
        productionPerTurnLabel.setText(I18N.formatFloat(productionPerTurn, "{0,number,0.0}"));
    }

    public ShipDesign getShipDesign() {
        return shipDesign;
    }
}
