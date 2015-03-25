package masterofgalaxy.world.ui.research;

import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.ui.ActorRemoveEscapeKeyAdapter;
import masterofgalaxy.ui.Windows;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class ResearchWindow extends Window implements Localizable {

    public ResearchWindow(Skin skin) {
        super("$research", skin);
        I18N.addLocalizable(this);
        addListener(new ActorRemoveEscapeKeyAdapter(this));

        setModal(true);
        Windows.addXButton(this, skin);

        Table table = new Table(skin);
        table.add(new Label("ho ho oh", skin));
        table.row();
        add(table).expand().fill();

        applyTranslation();
    }

    @Override
    public void applyTranslation() {
        setTitle(I18N.resolve("$research"));
    }

}
