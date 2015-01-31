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
import masterofgalaxy.ecs.components.DockComponent;
import masterofgalaxy.ecs.components.DockableComponent;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.PlayerOwnerComponent;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.world.FleetSplitter;
import masterofgalaxy.world.turns.FleetSameDockMerger;

public class FleetUi extends Table implements Localizable {
    private Skin skin;
    private Entity entity;
    private Label titleLabel;
    private Label ownerLabel;
    private FleetShipsUi shipsUi;
    private TextButton splitButton;
    private TextButton mergeButton;
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
        titleLabel = new Label("", skin);
        add(titleLabel).expandX();
        row();

        ownerLabel = new Label("", skin);
        add(ownerLabel).expandX();
        row();

        shipsUi = new FleetShipsUi(game, skin);
        add(shipsUi).expandX().fillX();
        row();

        setupSplitButton();
        setupMergeButton();
    }

    private void setupSplitButton() {
        splitButton = new TextButton("", skin);
        splitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FleetSplitter splitter = new FleetSplitter(game.getWorldScreen(), entity);
                shipsUi.fillInSplitter(splitter);
                Entity newFleet = splitter.split();
                if (newFleet != null) {
                    game.getWorldScreen().getPickLogic().setSelection(newFleet);
                }
            }
        });
        add(splitButton).expandX().fillX();
        row();
    }

    private void setupMergeButton() {
        mergeButton = new TextButton("", skin);
        mergeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DockableComponent dockable = Mappers.dockable.get(entity);
                Player owner = Mappers.playerOwner.get(entity).getOwner();
                game.getWorldScreen().getPickLogic().setSelection(null);
                FleetSameDockMerger merger = new FleetSameDockMerger(game.getWorldScreen());
                DockComponent dock = Mappers.dock.get(dockable.dockedAt);
                merger.mergeDock(dock);
                game.getWorldScreen().getPickLogic().setSelection(
                        PlayerOwnerComponent.pickFirstWherePlayerMatches(dock.dockedEntities, owner));
            }
        });
        add(mergeButton).expandX().fillX();
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
        splitButton.setVisible(canSplit());
        mergeButton.setVisible(canMerge());
    }

    private boolean isOwnedByCurrentPlayer() {
        return Mappers.playerOwner.get(entity).getOwner() == game.getWorldScreen().getCurrentPlayer();
    }

    private boolean canSplit() {
        return FleetSplitter.canSplitFleet(entity) && isOwnedByCurrentPlayer();
    }

    private boolean canMerge() {
        if (!isOwnedByCurrentPlayer()) {
            return false;
        }
        if (Mappers.dockable.has(entity)) {
            return FleetSameDockMerger.canMerge(Mappers.dock.get(Mappers.dockable.get(entity).dockedAt));
        }
        return false;
    }

    @Override
    public void applyTranslation() {
        setEntity(entity);
        splitButton.setText(I18N.resolve("$split"));
        mergeButton.setText(I18N.resolve("$merge"));
    }
}
