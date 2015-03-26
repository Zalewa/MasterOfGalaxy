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

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class ResearchWindow extends Window implements Localizable {
    private MogGame game;
    private Skin skin;
    private List<TechBox> techBoxes = new LinkedList<TechBox>();

    public ResearchWindow(MogGame game, Skin skin) {
        super("$research", skin);
        this.game = game;
        this.skin = skin;
        I18N.addLocalizable(this);
        addListener(new ActorRemoveEscapeKeyAdapter(this));

        setModal(true);
        Windows.addXButton(this, skin);

        Table table = new Table(skin);
        for (TechBranch branch : game.getActorAssets().tech.getBranches()) {
            table.add(mkTechBox(branch)).width(200.0f).height(200.0f).fill();
            table.row();
        }
        add(table).expand().fill();

        applyTranslation();
    }

    private TechBox mkTechBox(TechBranch branch) {
        TechKnowledge knowledge = game.getWorldScreen().getCurrentPlayer().getTechKnowledge();
        TechBox box = new TechBox(branch, knowledge, skin);
        techBoxes.add(box);
        return box;
    }

    public void refreshData() {
        for (TechBox box : techBoxes) {
            box.refreshData();
        }
    }

    @Override
    public void applyTranslation() {
        setTitle(I18N.resolve("$research"));
        for (TechBox box : techBoxes) {
            box.applyTranslation();
        }
    }
}
