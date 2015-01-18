package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import masterofgalaxy.ecs.components.ColonyComponent;
import masterofgalaxy.ecs.components.Mappers;

public class ShipyardUi extends Table {
    private Label nameLabel;
    private ProgressBar progressBar;
    private Skin skin;
    private Entity entity;

    public Signal<Object> shipyardProductionMenuRequested = new Signal<Object>();

    public ShipyardUi(Skin skin) {
        super(skin);
        this.skin = skin;

        setupUi();
    }

    public void setColonyEntity(Entity entity) {
        this.entity = entity;
        if (entity == null) {
            return;
        }
        ColonyComponent colony = Mappers.colony.get(entity);
        if (colony.shipyard.constructedShip != null) {
            nameLabel.setText(colony.shipyard.getConstructedShipName());
        } else {
            nameLabel.setText("N/A");
        }
        progressBar.setValue(colony.shipyard.getConstructedShipProgress());
    }

    private void setupUi() {
        setupNameLabel();
        setupProgressBar();
    }

    private void setupNameLabel() {
        nameLabel = new Label("", skin);
        nameLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                shipyardProductionMenuRequested.dispatch(null);
            }
        });
        add(nameLabel).expandX().center();
        row();
    }

    private void setupProgressBar() {
        progressBar = new ProgressBar(0.0f, 1.0f, 0.01f, false, skin);
        add(progressBar).expandX().fillX();
        row();
    }
}
