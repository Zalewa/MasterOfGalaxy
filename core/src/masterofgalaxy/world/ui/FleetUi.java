package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationChangedListener;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.PlayerOwnerComponent;
import masterofgalaxy.gamestate.savegame.FleetShipsState;
import masterofgalaxy.world.FleetSplitter;

public class FleetUi extends Table implements Localizable {
    private Skin skin;
    private Entity entity;
    private Label titleLabel;
    private Label ownerLabel;
    private FleetShipsUi shipsUi;
    private TextButton splitButton;
    private MogGame game;

    public FleetUi(MogGame game, Skin skin) {
        super(skin);
        this.game = game;
        this.skin = skin;
        I18N.localeChanged.add(new LocalizationChangedListener(this));

        setupHeader();

        setBackground("default-rect");
        setColor(1.0f, 1.0f, 1.0f, 1.0f);

        applyTranslation();
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
        table.row();

        setupSplitButton(table);

        add(table).expandX().fill();
        row();
    }

    private void setupSplitButton(Table table) {
        splitButton = new TextButton("", skin);
        splitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FleetSplitter splitter = new FleetSplitter(game.getWorldScreen(), entity);
                shipsUi.fillInSplitter(splitter);
                splitter.split();
            }
        });
        table.add(splitButton).expandX().fillX();
        table.row();
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
        splitButton.setText(I18N.resolve("$split"));
    }
}
