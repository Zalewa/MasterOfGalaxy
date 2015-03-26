package masterofgalaxy.world.ui.research;

import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.tech.TechBranch;
import masterofgalaxy.assets.tech.TechKnowledge;
import masterofgalaxy.ui.ListBox;
import masterofgalaxy.ui.Ui;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class TechBox extends Table {
    private Skin skin;
    private TechBranch techBranch;
    private TechKnowledge knowledge;
    private ListBox<String> list;
    private TextButton currentResearchButton;

    public TechBox(TechBranch techBranch, TechKnowledge knowledge, Skin skin) {
        this.skin = skin;
        this.techBranch = techBranch;
        this.knowledge = knowledge;

        defaults().space(5.0f);

        list = new ListBox<String>(skin);
        add(list).expand().fill().row();

        currentResearchButton = new TextButton("", skin);
        currentResearchButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                openPickResearchBox();
            }
        });
        add(currentResearchButton).expandX().fillX().row();
    }

    private void openPickResearchBox() {
        final ResearchPickWindow box = new ResearchPickWindow(techBranch, knowledge, skin);
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
        list.getList().setItems(knowledge.getTechs(techBranch.getId()).toArray(new String[0]));
        currentResearchButton.setText(getCurrentResearch());
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
