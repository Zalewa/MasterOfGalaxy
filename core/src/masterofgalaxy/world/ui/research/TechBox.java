package masterofgalaxy.world.ui.research;

import masterofgalaxy.assets.tech.TechBranch;
import masterofgalaxy.assets.tech.TechKnowledge;
import masterofgalaxy.ui.ListBox;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class TechBox extends Table {
    private Skin skin;
    private TechBranch techBranch;
    private TechKnowledge knowledge;
    private ListBox<String> list;

    public TechBox(TechBranch techBranch, TechKnowledge knowledge, Skin skin) {
        this.skin = skin;
        this.techBranch = techBranch;
        this.knowledge = knowledge;

        list = new ListBox<String>(skin);
        add(list).expand().row();

        list.getList().setItems(knowledge.getTechs(techBranch.getId()).toArray(new String[0]));
    }
}
