package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationChangedListener;
import masterofgalaxy.ecs.components.ColonyComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.world.PlayerOwnerControl;

public class ColonyUi extends Table implements Localizable {
    private MogGame game;
    private Skin skin;
    private Entity entity;
    private ResourcesDistributionUi resourcesDistributionUi;
    private Label populationLabel;
    private Label populationValueLabel;
    private Label defenseBasesLabel;
    private Label defenseBasesValueLabel;
    private Label factoriesLabel;
    private Label factoriesValueLabel;
    private Label productionLabel;
    private Label productionValueLabel;
    private ShipyardUi shipyardUi;

    public ColonyUi(MogGame game, Skin skin) {
        super(skin);
        I18N.localeChanged.add(new LocalizationChangedListener(this));
        this.game = game;
        this.skin = skin;

        defaults().expandX();
        pad(5.0f);

        setupUi();
        applyTranslation();
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        if (entity == null) {
            return;
        }
        ColonyComponent colony = Mappers.colony.get(entity);
        if (colony != null) {
            setColony(colony);
            resourcesDistributionUi.setColony(colony);
            shipyardUi.setColonyEntity(entity);

            resourcesDistributionUi.setVisible(canControl());
            shipyardUi.setVisible(canControl());
        }
    }

    private boolean canControl() {
        return PlayerOwnerControl.canControl(game.getWorldScreen(), entity);
    }

    private void setColony(ColonyComponent colony) {
        populationValueLabel.setText(I18N.formatFloat(colony.state.population, "{0,number,0.0}"));
        factoriesValueLabel.setText(I18N.formatFloat(colony.state.factories, "{0,number,0}"));
        defenseBasesValueLabel.setText(I18N.formatFloat(colony.state.defenseBases, "{0,number,0}"));
        productionValueLabel.setText(I18N.formatFloat(colony.getProduction(), "{0,number,0.0}"));
    }

    private void setupUi() {
        setupPopulation();
        setupFactoriesLabel();
        setupDefenseBasesLabel();
        setupProductionLabel();
        setupResourcesUi();
        setupShipyardUi();
    }

    private void setupPopulation() {
        populationLabel = new Label("", skin);
        add(populationLabel).left();
        populationValueLabel = new Label("", skin);
        add(populationValueLabel).right();
        row();
    }

    private void setupFactoriesLabel() {
        factoriesLabel = new Label("", skin);
        add(factoriesLabel).left();
        factoriesValueLabel = new Label("", skin);
        add(factoriesValueLabel).right();
        row();
    }

    private void setupDefenseBasesLabel() {
        defenseBasesLabel = new Label("", skin);
        add(defenseBasesLabel).left();
        defenseBasesValueLabel = new Label("", skin);
        add(defenseBasesValueLabel).right();
        row();
    }

    private void setupProductionLabel() {
        productionLabel = new Label("", skin);
        add(productionLabel).left();
        productionValueLabel = new Label("", skin);
        add(productionValueLabel).right();
        row();
    }

    private void setupResourcesUi() {
        resourcesDistributionUi = new ResourcesDistributionUi(skin);
        add(resourcesDistributionUi).expandX().fillX().colspan(2);
        row();
    }

    private void setupShipyardUi() {
        shipyardUi = new ShipyardUi(skin);
        add(shipyardUi).colspan(2).expandX().fill();
        row();
    }

    public ShipyardUi getShipyardUi() {
        return shipyardUi;
    }

    @Override
    public void applyTranslation() {
        populationLabel.setText(I18N.resolve("$population_colon"));
        productionLabel.setText(I18N.resolve("$production_colon"));
        defenseBasesLabel.setText(I18N.resolve("$bases_colon"));
        factoriesLabel.setText(I18N.resolve("$factories_colon"));
        setEntity(entity);
    }
}
