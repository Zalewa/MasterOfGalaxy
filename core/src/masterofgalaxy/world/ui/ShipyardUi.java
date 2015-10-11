package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationChangedListener;
import masterofgalaxy.ecs.components.ColonyComponent;
import masterofgalaxy.ecs.components.Mappers;

public class ShipyardUi extends Table implements Localizable {
    private Label nameLabel;
    private ProgressBar progressBar;
    private Entity entity;

    public Signal<Object> shipyardProductionMenuRequested = new Signal<Object>();

    public ShipyardUi(Skin skin) {
        super(skin);
        I18N.localeChanged.add(new LocalizationChangedListener(this));
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
            nameLabel.setText(I18N.resolve("$n/a"));
        }
        progressBar.setValue(colony.shipyard.getConstructedShipProgress());
    }

    private void setupUi() {
        setupNameLabel();
        setupProgressBar();
    }

    private void setupNameLabel() {
        nameLabel = new Label("", getSkin());
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
        progressBar = new ProgressBar(0.0f, 1.0f, 0.01f, false, getSkin());
        add(progressBar).expandX().fillX();
        row();
    }

    @Override
    public void applyTranslation() {
        setColonyEntity(entity);
    }
}
