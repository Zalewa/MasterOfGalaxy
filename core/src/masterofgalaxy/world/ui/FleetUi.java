package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationChangedListener;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.PlayerOwnerComponent;
import masterofgalaxy.gamestate.savegame.FleetShipsState;

public class FleetUi extends Table implements Localizable {
    private Skin skin;
    private Entity entity;
    private Label titleLabel;
    private Label ownerLabel;
    private FleetShipsUi shipsUi;
    private MogGame game;

    public FleetUi(MogGame game, Skin skin) {
        super(skin);
        this.game = game;
        this.skin = skin;
        I18N.localeChanged.add(new LocalizationChangedListener(this));

        setupHeader();

        setBackground("default-rect");
        setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void setupHeader() {
        Table table = new Table(skin);

        titleLabel = new Label("", skin);
        table.add(titleLabel).expandX();
        table.row();

        ownerLabel = new Label("", skin);
        table.add(ownerLabel).expandX();
        table.row();

        shipsUi = new FleetShipsUi(skin);
        table.add(shipsUi).expandX().fillX();

        add(table).expandX().fill();
        row();
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        if (entity == null) {
            return;
        }
        PlayerOwnerComponent owner = Mappers.playerOwner.get(entity);
        titleLabel.setText(I18N.resolve("$fleet"));
        ownerLabel.setColor(owner.getOwner().getColor());
        ownerLabel.setText(owner.getOwner().getName());
        shipsUi.setEntity(entity);
    }

    @Override
    public void applyTranslation() {
        setEntity(entity);
    }
}
