package masterofgalaxy.world.ui.research;

import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.I18NFormsResolver;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.tech.Tech;
import masterofgalaxy.assets.tech.TechBranch;
import masterofgalaxy.assets.tech.TechKnowledge;
import masterofgalaxy.ui.ActorRemoveEscapeKeyAdapter;
import masterofgalaxy.ui.DoubleClickAdapter;
import masterofgalaxy.ui.ListBox;
import masterofgalaxy.ui.Windows;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class ResearchPickWindow extends Window implements Localizable {
    public Signal<Object> techPickedSignal = new Signal<Object>();

    private Skin skin;
    private TechBranch branch;
    private TechKnowledge knowledge;

    private Label titleLabel;
    private ListBox<Tech> techList;
    private Label descriptionLabel;

    public ResearchPickWindow(TechBranch branch, TechKnowledge knowledge, Skin skin) {
        super("", skin);
        this.skin = skin;
        this.branch = branch;
        this.knowledge = knowledge;

        setup();
        fillIn();
        applyTranslation();
    }

    private void setup() {
        addListener(new ActorRemoveEscapeKeyAdapter(this));
        Windows.addXButton(this, skin);
        setModal(true);

        defaults().space(5.0f);

        titleLabel = new Label("", skin);
        add(titleLabel).expandX().left().row();

        setupTechList();

        descriptionLabel = new Label("", skin);
        descriptionLabel.setAlignment(Align.topLeft);
        descriptionLabel.setWrap(true);
        add(descriptionLabel).width(400.0f).height(100.0f).expandX().top().row();
    }

    private void setupTechList() {
        techList = new ListBox<Tech>(skin);
        techList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateDescripiton();
            }
        });
        techList.addListener(new DoubleClickAdapter() {
            @Override
            protected void doubleClicked(InputEvent event, float x, float y) {
                pickTech();
            }
        });
        add(techList).width(400.0f).height(200.0f).fill().row();
    }

    private void updateDescripiton() {
        if (techList.getSelected() != null) {
            descriptionLabel.setText(techList.getSelected().getDescription());
        } else {
            descriptionLabel.setText("");
        }
    }

    private void pickTech() {
        if (techList.getSelected() != null) {
            knowledge.startResearch(branch, techList.getSelected());
        }
        techPickedSignal.dispatch(this);
        remove();
    }

    private void fillIn() {
        techList.setItems(branch.getResearchableTechs(knowledge).toArray(new Tech[0]));
        descriptionLabel.setText("");

        selectCurrentlyResearchedTech();
        updateDescripiton();
    }

    private void selectCurrentlyResearchedTech() {
        String currentTech = knowledge.getCurrentResearchOnBranch(branch);
        if (currentTech == null) {
            return;
        }
        for (int i = 0; i < techList.getItems().size; ++i) {
            Tech tech = techList.getItems().get(i);
            if (tech.getId().equals(currentTech)) {
                techList.setSelected(tech);
            }
        }
    }

    @Override
    public void applyTranslation() {
        getTitleLabel().setText(I18N.resolve("$research"));

        String branchName = I18NFormsResolver.resolve(branch.getId(),
                branch.getLocalizationBundleName(),
                I18NFormsResolver.Form.Genitive);
        titleLabel.setText(I18N.resolve("$pickResearch", branchName));
    }
}
