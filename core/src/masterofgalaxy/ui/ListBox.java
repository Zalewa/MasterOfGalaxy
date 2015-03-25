package masterofgalaxy.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ListBox<T> extends Table {
    private Skin skin;
    private Label label;
    private List<T> list;
    private ScrollPane scrollPane;

    public ListBox(Skin skin) {
        super(skin);
        this.skin = skin;

        setup();
    }

    private void setup() {
        label = new Label("", skin);
        add(label).expandX().row();

        list = new List<T>(skin);

        scrollPane = new ScrollPane(list, skin);
        add(scrollPane).expand().row();
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public List<T> getList() {
        return list;
    }
}
