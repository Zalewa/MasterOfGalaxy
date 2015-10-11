package masterofgalaxy.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class ListBox<T> extends Table {
    private Label label;
    private List<T> list;
    private ScrollPane scrollPane;

    public ListBox(Skin skin) {
        super(skin);
        setup();
    }

    private void setup() {
        label = new Label("", getSkin());
        add(label).expandX().row();

        list = new List<T>(getSkin());

        scrollPane = new ScrollPane(list, getSkin());
        add(scrollPane).expand().fill().row();
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public List<T> getList() {
        return list;
    }

    public Array<T> getItems() {
        return list.getItems();
    }

    public void setItems(T[] array) {
        list.setItems(array);
    }

    public void setSelected(T item) {
        list.setSelected(item);
    }

    public T getSelected() {
        return list.getSelected();
    }
}
