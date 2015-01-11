package masterofgalaxy.world.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationChangedListener;
import masterofgalaxy.ecs.components.ColonyComponent;
import masterofgalaxy.world.stars.MainResourceDistribution;
import masterofgalaxy.world.stars.MainResourceDistribution.ResourceId;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResourcesDistributionUi extends Table implements Localizable {
    private final float sliderPool = 1.0f;

    private Skin skin;
    private Label header;
    private ColonyComponent colony;
    private Table slidersTable;
    private Map<ResourceId, ResourceDistributionSlider> mainResourceSliders
            = new LinkedHashMap<ResourceId, ResourceDistributionSlider>();
    private MainSliderChangeAdapter mainResourceSliderChangeAdapter = new MainSliderChangeAdapter();

    public ResourcesDistributionUi(Skin skin) {
        super(skin);
        this.skin = skin;
        I18N.localeChanged.add(new LocalizationChangedListener(this));

        setupUi();
        applyTranslation();
    }

    private void setupUi() {
        setupHeader();
        setupSliders();
    }

    private void setupHeader() {
        header = new Label("", skin);
        add(header).expandX().center();
        row();
    }


    private void setupSliders() {
        slidersTable = new Table(skin);
        slidersTable.defaults().space(3.0f);
        add(slidersTable).expandX().fillX().width(this.getWidth());
        row();

        mkMainResourceSlider(ResourceId.Shipyard);
        mkMainResourceSlider(ResourceId.Defense);
        mkMainResourceSlider(ResourceId.Industry);
        mkMainResourceSlider(ResourceId.Ecology);
        mkMainResourceSlider(ResourceId.Research);
    }

    private void mkMainResourceSlider(ResourceId resourceId) {
        ResourceDistributionSlider slider = new ResourceDistributionSlider(skin);
        addResourceSliderToTable(slidersTable, slider);
        registerMainResourceSlider(slider);
        mainResourceSliders.put(resourceId, slider);
    }

    private void registerMainResourceSlider(ResourceDistributionSlider slider) {
        slider.addValueChangedListener(mainResourceSliderChangeAdapter);
        slider.addLabelClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ResourceId id = findMainResourceId(event.getTarget());
                colony.state.mainResourceDistribution.setLocked(id, !colony.state.mainResourceDistribution.isLocked(id));
                updateMainResourceSliders();
            }
        });
    }

    private void addResourceSliderToTable(Table table, ResourceDistributionSlider slider) {
        table.add(slider.getLabel()).left();
        table.add(slider.getSlider()).maxWidth(100.0f).prefWidth(100.0f).width(100.0f).fillX();
        table.add(slider.getValueLabel()).expandX().right();
        table.row();
    }

    @Override
    public void applyTranslation() {
        header.setText(I18N.resolve("$production"));
        for (ResourceId resourceId : mainResourceSliders.keySet()) {
            ResourceDistributionSlider slider = mainResourceSliders.get(resourceId);
            slider.setLabelText(I18N.resolve(resourceId.getLocalization()));
        }
    }

    public void setColony(ColonyComponent colony) {
        this.colony = colony;
        updateMainResourceSliders();
    }

    private ResourceId findMainResourceId(Actor actor) {
        for (ResourceId resourceId : mainResourceSliders.keySet()) {
            ResourceDistributionSlider slider = mainResourceSliders.get(resourceId);
            if (slider.isActorMine(actor)) {
                return resourceId;
            }
        }
        return null;
    }

    private void updateMainResourceSliders() {
        mainResourceSliderChangeAdapter.enabled = false;
        for (ResourceId resourceId : mainResourceSliders.keySet()) {
            ResourceDistributionSlider slider = mainResourceSliders.get(resourceId);
            slider.setSliderValue(colony.state.mainResourceDistribution.getAmount(resourceId));
            slider.setLocked(colony.state.mainResourceDistribution.isLocked(resourceId));
        }
        mainResourceSliderChangeAdapter.enabled = true;
    }

    private class MainSliderChangeAdapter extends ChangeListener {
        private boolean enabled = true;

        @Override
        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            if (!enabled) {
                return;
            }
            ResourceId id = findMainResourceId(actor);
            colony.state.mainResourceDistribution.setAmount(id, mainResourceSliders.get(id).getSliderValue());
            updateMainResourceSliders();
        }
    }
}
