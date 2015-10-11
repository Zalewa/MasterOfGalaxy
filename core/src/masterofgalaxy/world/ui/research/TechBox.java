package masterofgalaxy.world.ui.research;

import masterofgalaxy.MogGame;
import masterofgalaxy.assets.Sprite;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.tech.Tech;
import masterofgalaxy.assets.tech.TechBranch;
import masterofgalaxy.assets.tech.TechKnowledge;
import masterofgalaxy.ui.ListBox;
import masterofgalaxy.ui.Ui;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class TechBox extends Table {
    public Signal<TechBox> distributionChangedSignal = new Signal<TechBox>();

    private TechBranch techBranch;
    private TechKnowledge knowledge;
    private ListBox<Tech> list;
    private TextButton currentResearchButton;
    private Table resourceTable;
    private Slider resourceSlider;
    private Label progressLabel;
    private ImageButton lockButton;
    private MogGame game;

    public TechBox(TechBranch techBranch, TechKnowledge knowledge, MogGame game, Skin skin) {
        super(skin);
        this.techBranch = techBranch;
        this.knowledge = knowledge;
        this.game = game;

        defaults().space(5.0f);

        list = new ListBox<Tech>(skin);
        add(list).expand().fill().row();

        currentResearchButton = new TextButton("", skin);
        currentResearchButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                openPickResearchBox();
            }
        });
        add(currentResearchButton).expandX().fillX().row();

        setupResourceTable();
    }

    private void setupResourceTable() {
        resourceTable = new Table(getSkin());
        resourceTable.defaults().space(5.0f);

        setupResourceLockedButton();
        setupResourceSlider();
        setupProgressLabel();

        add(resourceTable).expandX().fill().row();
    }

    private void setupResourceLockedButton() {
        lockButton = new ImageButton(getUnlockedImage(), null, getLockedImage());
        lockButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                knowledge.setBranchResourceLocked(techBranch, !knowledge.isBranchResourceLocked(techBranch));
                distributionChangedSignal.dispatch(TechBox.this);
            }
        });
        resourceTable.add(lockButton).width(16.0f).height(16.0f);
    }

    private Drawable getLockedImage() {
        return wrapDrawable(Sprite.padlockClosed);
    }

    private Drawable getUnlockedImage() {
        return wrapDrawable(Sprite.padlockOpen);
    }

    private Drawable wrapDrawable(AssetDescriptor<Texture> texture) {
        return new TextureRegionDrawable(new TextureRegion(game
                .getAssetManager().get(texture)));
    }

    private void setupResourceSlider() {
        resourceSlider = new Slider(0.0f, 1.0f, 0.01f, false, getSkin());
        resourceSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                knowledge.setBranchResourceDistribution(techBranch, resourceSlider.getValue());
                distributionChangedSignal.dispatch(TechBox.this);
            }
        });
        resourceTable.add(resourceSlider).expandX().fillX();
    }

    private void setupProgressLabel() {
        progressLabel = new Label("", getSkin());
        progressLabel.setAlignment(Align.right);
        resourceTable.add(progressLabel).width(40.0f);
    }

    private void openPickResearchBox() {
        final ResearchPickWindow box = new ResearchPickWindow(techBranch, knowledge, getSkin());
        box.techPickedSignal.add(new Listener<Object>() {
            @Override
            public void receive(Signal<Object> signal, Object object) {
                refreshData();
                box.techPickedSignal.remove(this);
            }
        });
        getStage().addActor(box);
        getStage().setKeyboardFocus(box);
        box.pack();
        box.setVisible(true);
        Ui.centerWithinStage(box);
    }

    public void refreshData() {
        list.getList().setItems(knowledge.getTechs(techBranch).toArray(new Tech[0]));
        currentResearchButton.setText(getCurrentResearch());
        resourceSlider.setValue(knowledge.getBranchResourceDistribution(techBranch));
        lockButton.setChecked(knowledge.isBranchResourceLocked(techBranch));
        if (knowledge.getCurrentResearchOnBranch(techBranch) != null) {
            progressLabel.setText(I18N.formatFloat(
                knowledge.getCurrentResearchCostProgressOnBranch(techBranch) * 100.0f, "{0,number,0}%"));
        } else {
            progressLabel.setText(I18N.resolve("$n/a"));
        }
    }

    private String getCurrentResearch() {
        String research = knowledge.getCurrentResearchOnBranch(techBranch);
        return research != null ? I18N.resolveNamed(
            techBranch.getLocalizationBundleName(), research) : I18N.resolve("$n/a");
    }

    public void applyTranslation() {
        list.setLabel(I18N.resolveNamed(techBranch.getLocalizationBundleName(), techBranch.getId()));
        refreshData();
    }
}
