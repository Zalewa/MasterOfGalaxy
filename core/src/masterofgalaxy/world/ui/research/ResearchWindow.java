package masterofgalaxy.world.ui.research;

import java.util.LinkedList;
import java.util.List;

import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.tech.TechBranch;
import masterofgalaxy.assets.tech.TechKnowledge;
import masterofgalaxy.ui.ActorRemoveEscapeKeyAdapter;
import masterofgalaxy.ui.Windows;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class ResearchWindow extends Window implements Localizable {
    private MogGame game;
    private List<TechBox> techBoxes = new LinkedList<TechBox>();
    private Container<Table> techBoxesContainer;
    private Listener<TechBox> distributionChangeListener;

    public ResearchWindow(MogGame game, Skin skin) {
        super("$research", skin);
        this.game = game;
        I18N.addLocalizable(this);
        addListener(new ActorRemoveEscapeKeyAdapter(this));

        setModal(true);
        Windows.addXButton(this, skin);

        techBoxesContainer = new Container<Table>(null);
        add(techBoxesContainer).expand().fill();
    }

    private TechBox mkTechBox(TechBranch branch) {
        TechKnowledge knowledge = game.getWorldScreen().getCurrentPlayer().getTechKnowledge();
        TechBox box = new TechBox(branch, knowledge, game, getSkin());
        box.distributionChangedSignal.add(getDistributionChangeListener());
        techBoxes.add(box);
        return box;
    }

    public void refreshData() {
        deregisterTechBoxes();
        Table table = new Table(getSkin());
        for (TechBranch branch : game.getWorldScreen().getCurrentPlayer().getTechTree().getBranches()) {
            table.add(mkTechBox(branch)).width(300.0f).height(200.0f).fill();
            table.row();
        }
        add(table).expand().fill();
        techBoxesContainer.setActor(table);
        for (TechBox box : techBoxes) {
            box.refreshData();
        }

        applyTranslation();
    }

    private Listener<TechBox> getDistributionChangeListener() {
        if (distributionChangeListener == null) {
            distributionChangeListener = new Listener<TechBox>() {
                @Override
                public void receive(Signal<TechBox> signal, TechBox object) {
                    updateTechBoxes();
                }
            };
        }
        return distributionChangeListener;
    }

    private void updateTechBoxes() {
        for (TechBox box : techBoxes) {
            box.refreshData();
        }
    }

    private void deregisterTechBoxes() {
        for (TechBox box : techBoxes) {
            box.distributionChangedSignal.remove(getDistributionChangeListener());
        }
        techBoxes.clear();
    }

    @Override
    public void applyTranslation() {
        getTitleLabel().setText(I18N.resolve("$research"));
        for (TechBox box : techBoxes) {
            box.applyTranslation();
        }
    }
}
