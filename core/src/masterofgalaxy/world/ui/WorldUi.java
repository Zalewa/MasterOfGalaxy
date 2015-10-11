package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import masterofgalaxy.assets.UiSkin;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.draw.Pixel;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.gamestate.ships.ShipDesign;
import masterofgalaxy.ui.ConsumeTouchAdapter;
import masterofgalaxy.world.WorldScreen;

public class WorldUi implements Disposable {
    private Stage stage;
    private WorldScreen worldScreen;
    private EntitySelectionListener entitySelectionListener = new EntitySelectionListener();

    private Container<Actor> infoUiContainer;
    private StarUi starUi;
    private FleetUi fleetUi;
    private ShipyardProductionUi shipyardProductionUi;
    private Table mainLayout;
    private Table bottomLayout;
    private Label turnLabel;
    private Image playerIndicator;
    private TextButton researchButton;
    private TextButton nextTurnButton;
    private TextButton mainMenuButton;

    public WorldUi(WorldScreen worldScreen) {
        this.worldScreen = worldScreen;
        stage = new Stage(new ScreenViewport());

        setupMainLayout();
        setupInfoUi();
        setupStarUi();
        setupFleetUi();
        setupShipyardProductionUi();
        setupBottomLayout();
        setupTurnLabel();
        setupButtons();

        mainLayout.layout();

        registerEvents();
        applyTranslation();
    }

    private void setupMainLayout() {
        mainLayout = new Table(getSkin());
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

    private void setupShipyardProductionUi() {
        shipyardProductionUi = new ShipyardProductionUi(worldScreen.getGame(), getSkin());
        shipyardProductionUi.closeRequested.add(new Listener<Object>() {
            @Override
            public void receive(Signal<Object> signal, Object object) {
                showSelectionUiForCurrentlySelectedEntity();
            }
        });
        shipyardProductionUi.designPicked.add(new Listener<ShipDesign>() {
            @Override
            public void receive(Signal<ShipDesign> signal, ShipDesign design) {
                showSelectionUiForCurrentlySelectedEntity();
            }
        });
    }

    private void showSelectionUiForCurrentlySelectedEntity() {
        updateSelectionUi(worldScreen.getPickLogic().getSelectedEntity());
    }

    private void setupStarUi() {
        starUi = new StarUi(worldScreen.getGame(), getSkin());
        starUi.getColonyUi().getShipyardUi().shipyardProductionMenuRequested.add(new Listener<Object>() {
            @Override
            public void receive(Signal<Object> signal, Object object) {
                showShipyardProductionForCurrentlySelectedEntity();
            }
        });
    }

    private void showShipyardProductionForCurrentlySelectedEntity() {
        infoUiContainer.removeActor(infoUiContainer.getActor());
        infoUiContainer.setActor(shipyardProductionUi);
        shipyardProductionUi.setEntity(worldScreen.getPickLogic().getSelectedEntity());
    }

    private void setupFleetUi() {
        fleetUi = new FleetUi(worldScreen.getGame(), getSkin());
    }

    private void setupBottomLayout() {
        bottomLayout = new Table(getSkin());
        mainLayout.add(bottomLayout).expand().fillX().center().bottom();
    }

    private void setupTurnLabel() {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(Pixel.getPixel()));
        playerIndicator = new Image(drawable, Scaling.fill);
        playerIndicator.setColor(1.0f, 0.0f, 0.0f, 1.0f);

        turnLabel = new Label("$turnNo", getSkin());

        Table row = new Table(getSkin());
        row.defaults().space(5.0f);
        row.pad(5.0f);
        row.add(playerIndicator).width(16.0f).height(16.0f).center();
        row.add(turnLabel).expandX().fillX();

        bottomLayout.add(row).expandX().fillX();
        bottomLayout.row();
    }

    private void setupButtons() {
        setupResearchButton();
        setupNextTurnButton();
        setupMainMenuButton();
    }

    private void setupResearchButton() {
        researchButton = new TextButton("$research", getSkin());
        researchButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                worldScreen.getGlobalUi().openResearchScreen();
            }
        });
        bottomLayout.add(researchButton).expandX().fillX();
        bottomLayout.row();
    }

    private void setupNextTurnButton() {
        nextTurnButton = new TextButton("$nextTurn", getSkin());
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
        mainMenuButton = new TextButton("Main Menu", getSkin());
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

    @Override
    public void dispose() {
        worldScreen.selectionChanged.remove(entitySelectionListener);
    }

    public void reset() {
        updateSelectionUi(null);
    }

    public void startTurnProcessing() {
        nextTurnButton.setVisible(false);
        mainMenuButton.setVisible(false);
        mainLayout.setTouchable(Touchable.disabled);
    }

    public void endTurnProcessing() {
        nextTurnButton.setVisible(true);
        mainMenuButton.setVisible(true);
        mainLayout.setTouchable(Touchable.enabled);
        updateTurnLabel();
    }

    public void updateTurnIndicators() {
        playerIndicator.setColor(worldScreen.getCurrentPlayer().getColor());
        updateTurnLabel();
    }

    private void updateTurnLabel() {
        turnLabel.setText(I18N.resolve("$turnNo", worldScreen.getTurn()));
    }

    private void applyTranslation() {
        updateTurnLabel();
        researchButton.setText(I18N.resolve("$research"));
        nextTurnButton.setText(I18N.resolve("$nextTurn"));
        mainMenuButton.setText(I18N.resolve("$mainMenu"));
    }

    private class EntitySelectionListener implements Listener<Entity> {
        @Override
        public void receive(Signal<Entity> signal, Entity object) {
            updateSelectionUi(object);
        }
    }

    private Skin getSkin() {
        return UiSkin.skin;
    }
}
