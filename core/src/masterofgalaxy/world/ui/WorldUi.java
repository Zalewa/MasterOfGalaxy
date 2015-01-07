package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import masterofgalaxy.assets.UiSkin;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ui.ConsumeTouchAdapter;
import masterofgalaxy.world.WorldScreen;

public class WorldUi implements Disposable {
    private Skin skin;
    private Stage stage;
    private WorldScreen worldScreen;
    private EntitySelectionListener entitySelectionListener = new EntitySelectionListener();

    private Container<Actor> infoUiContainer;
    private StarUi starUi;
    private FleetUi fleetUi;
    private Table mainLayout;
    private Table bottomLayout;
    private Label turnLabel;
    private TextButton nextTurnButton;
    private TextButton mainMenuButton;

    public WorldUi(WorldScreen worldScreen) {
        this.worldScreen = worldScreen;
        skin = UiSkin.skin;
        stage = new Stage(new ScreenViewport());

        setupMainLayout();
        setupInfoUi();
        setupStarUi();
        setupFleetUi();
        setupBottomLayout();
        setupTurnLabel();
        setupButtons();

        mainLayout.layout();

        registerEvents();
        applyTranslation();
    }

    private void setupMainLayout() {
        mainLayout = new Table(skin);
        mainLayout.setFillParent(true);
        mainLayout.setBackground("default-rect");
        stage.addActor(mainLayout);
    }

    private void setupInfoUi() {
        infoUiContainer = new Container<Actor>();
        infoUiContainer.fill();
        mainLayout.add(infoUiContainer).expand().fillX().center().top();
        mainLayout.row();
    }

    private void setupStarUi() {
        starUi = new StarUi(worldScreen.getGame(), skin);
    }

    private void setupFleetUi() {
        fleetUi = new FleetUi(worldScreen.getGame(), skin);
    }

    private void setupBottomLayout() {
        bottomLayout = new Table(skin);
        mainLayout.add(bottomLayout).expand().fillX().center().bottom();
    }

    private void setupTurnLabel() {
        turnLabel = new Label("$turnNo", skin);
        bottomLayout.add(turnLabel).expandX().fillX();
        bottomLayout.row();
    }

    private void setupButtons() {
        setupNextTurnButton();
        setupMainMenuButton();
    }

    private void setupNextTurnButton() {
        nextTurnButton = new TextButton("$nextTurn", skin);
        nextTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                worldScreen.endTurn();
            }
        });
        bottomLayout.add(nextTurnButton).expandX().fillX();
        bottomLayout.row();
    }

    private void setupMainMenuButton() {
        mainMenuButton = new TextButton("Main Menu", skin);
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                worldScreen.getGlobalUi().setMainMenuVisible(true);
            }
        });
        bottomLayout.add(mainMenuButton).expandX().fillX();
        bottomLayout.row();
    }

    public void act(float delta) {
        stage.act(delta);
    }

    public void draw() {
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    private void registerEvents() {
        stage.addListener(new ConsumeTouchAdapter());
        worldScreen.selectionChanged.add(entitySelectionListener);

        I18N.localeChanged.add(new Listener<Object>() {
            @Override
            public void receive(Signal<Object> signal, Object object) {
                applyTranslation();
            }
        });
    }

    private void updateSelectionUi(Entity entity) {
        infoUiContainer.removeActor(infoUiContainer.getActor());
        if (entity == null) {
            return;
        }

        if (Mappers.star.has(entity)) {
            infoUiContainer.setActor(starUi);
            starUi.setEntity(entity);
        } else if (Mappers.fleet.has(entity)) {
            infoUiContainer.setActor(fleetUi);
            fleetUi.setEntity(entity);
        }
    }

    public void dispose() {
        worldScreen.selectionChanged.remove(entitySelectionListener);
    }

    public void reset() {
        updateSelectionUi(null);
    }

    public void startTurnProcessing() {
        nextTurnButton.setVisible(false);
        mainMenuButton.setVisible(false);
    }

    public void endTurnProcessing() {
        nextTurnButton.setVisible(true);
        mainMenuButton.setVisible(true);
        updateTurnLabel();
    }

    public void updateTurnLabel() {
        turnLabel.setText(I18N.resolve("$turnNo", worldScreen.getTurn()));
    }

    private void applyTranslation() {
        updateTurnLabel();
        nextTurnButton.setText(I18N.resolve("$nextTurn"));
        mainMenuButton.setText(I18N.resolve("$mainMenu"));
    }

    private class EntitySelectionListener implements Listener<Entity> {
        @Override
        public void receive(Signal<Entity> signal, Entity object) {
            updateSelectionUi(object);
        }
    }
}
