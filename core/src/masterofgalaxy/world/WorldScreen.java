package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import masterofgalaxy.MogGame;
import masterofgalaxy.ecs.EntityPicker;
import masterofgalaxy.ecs.systems.*;
import masterofgalaxy.exceptions.SavedGameException;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.gamestate.savegame.WorldState;
import masterofgalaxy.world.picking.PickLogic;
import masterofgalaxy.world.turns.TurnProcessor;
import masterofgalaxy.world.ui.GlobalUi;
import masterofgalaxy.world.ui.WorldUi;
import masterofgalaxy.world.worldbuild.RectangleWorldStarLayout;
import masterofgalaxy.world.worldbuild.WorldBuilder;

public class WorldScreen extends ScreenAdapter {
    private MogGame game;
    private PooledEngine entityEngine;
    private WorldBackground background;
    private WorldCamera camera;
    private World world;
    private WorldUi ui;
    private GlobalUi globalUi;
    private WorldInputProcessor inputProcessor =  new WorldInputProcessor(this);
    private PickLogic pickLogic = null;
    private TurnProcessor turnProcessor;
    private ExtendViewport viewport;
    private InputMultiplexer inputMultiplexer = null;
    private Listener<Entity> selectionChangedListener;
    private Player currentPlayer;
    private DebugMode debugMode = new DebugMode();

    public Signal<Entity> selectionChanged = new Signal<Entity>();

    public WorldScreen(MogGame game) {
        this.game = game;

        selectionChangedListener = new Listener<Entity>() {
            @Override
            public void receive(Signal<Entity> signal, Entity object) {
                selectionChanged.dispatch(object);
            }
        };

        ui = new WorldUi(this);
        globalUi = new GlobalUi(game);
        globalUi.setCanResumeGame(false);
        camera = new WorldCamera(this);
        viewport = new ExtendViewport(1000.0f, 1000.0f, camera.getCamera());
        pickLogic = new PickLogic(this);
        background = new WorldBackground(this);

        entityEngine = new PooledEngine();
        entityEngine.addSystem(new TargetPurgingSystem());
        entityEngine.addSystem(new BlinkSystem());
        entityEngine.addSystem(new SelectionScalingSystem());
        entityEngine.addSystem(new DockPositioningSystem());
        entityEngine.addSystem(new DockCleanupSystem());
        entityEngine.addSystem(new MoveToTargetSystem());
        entityEngine.addSystem(new ParentshipSystem());
        entityEngine.addSystem(new RenderingSystem(game));
        entityEngine.addSystem(new TargetDrawSystem(game));
        entityEngine.addSystem(new TextRenderingSystem(game));
    }

    public void startNewGame() {
        resetGame();
        createNewWorld();
        postWorldBuildActions();
    }

    public void restoreGame(WorldState state) throws SavedGameException {
        resetGame();
        new WorldStateRestorer(this).restore(state);
        postWorldBuildActions();
    }

    private void createNewWorld() {
        WorldBuilder builder = new WorldBuilder(this, new RectangleWorldStarLayout(this), System.currentTimeMillis());
        builder.setNumUnownedStars(20);
        builder.setPlayField(new Rectangle(0.0f, 0.0f, 1200.0f, 1000.0f));
        builder.setNumPlayers(4);
        world = builder.build();
    }

    private void postWorldBuildActions() {
        resetActorPicker();
        resetCamera();
        ui.updateTurnIndicators();
    }

    private void resetActorPicker() {
        pickLogic.dispose();
        pickLogic = new PickLogic(this);
        pickLogic.addSelectionChangedListener(selectionChangedListener);
    }

    private void resetCamera() {
        viewport.setMinWorldWidth(world.getPlayField().getWidth());
        viewport.setMinWorldHeight(world.getPlayField().getHeight());
        camera.resetZoom();
        camera.centerCamera();
    }

    @Override
    public void render(float delta) {
        turnProcessor.update(delta);
        renderBackground(delta);
        renderEngine(delta);
        renderUi(delta);
        renderGlobalUi(delta);
    }

    private void renderBackground(float delta) {
        background.render(delta);
    }

    private void renderEngine(float delta) {
        updateRenderingTranslation();
        applyTextRenderingTranslation();
        entityEngine.update(delta);
    }

    private void updateRenderingTranslation() {
        RenderingSystem render = entityEngine.getSystem(RenderingSystem.class);
        render.setCamera(camera.getCamera());
        render.setViewport(viewport);

        TargetDrawSystem targetDraw = entityEngine.getSystem(TargetDrawSystem.class);
        targetDraw.setCamera(camera.getCamera());
        targetDraw.setViewport(viewport);
    }

    private void applyTextRenderingTranslation() {
        TextRenderingSystem render = entityEngine.getSystem(TextRenderingSystem.class);
        render.setGameViewport(viewport);
        render.setUiCamera(globalUi.getStage().getCamera());
        render.setUiViewport(globalUi.getStage().getViewport());
    }

    private void renderUi(float delta) {
        ui.getStage().getViewport().apply();
        ui.act(delta);
        ui.draw();
    }

    private void renderGlobalUi(float delta) {
        globalUi.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        final int uiWidth = 200;

        super.resize(width, height);
        viewport.update(width - uiWidth, height);
        ui.getStage().getViewport().update(uiWidth, height, true);
        ui.getStage().getViewport().setScreenPosition(width - uiWidth, 0);
        globalUi.updateScreenSize(width, height);
    }

    @Override
    public void dispose() {
        selectionChanged.removeAllListeners();
        resetEntityEngine();
        ui.dispose();
        removeInputHandlerFromGame();
        super.dispose();
    }

    @Override
    public void show() {
        super.show();
        removeInputHandlerFromGame();
        inputMultiplexer = new InputMultiplexer(globalUi.getStage(), ui.getStage(), inputProcessor);
        game.getInputMultiplexer().addProcessor(inputMultiplexer);
        globalUi.setMainMenuVisible(false);
    }

    @Override
    public void hide() {
        removeInputHandlerFromGame();
        super.hide();
    }

    private void removeInputHandlerFromGame() {
        if (inputMultiplexer != null) {
            game.getInputMultiplexer().removeProcessor(inputMultiplexer);
            inputMultiplexer = null;
        }
    }

    public PooledEngine getEntityEngine() {
        return entityEngine;
    }

    public MogGame getGame() {
        return game;
    }

    public World getWorld() {
        return world;
    }

    public WorldCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    private void resetGame() {
        if (world != null) {
            world.dispose();
        }
        world = new World(this);
        ui.reset();
        resetEntityEngine();
        resetTurnProcessor();
    }

    private void resetEntityEngine() {
        entityEngine.removeAllEntities();
        entityEngine.clearPools();
        entityEngine.getSystem(MoveToTargetSystem.class).setProcessing(false);
    }

    private void resetTurnProcessor() {
        if (turnProcessor != null) {
            turnProcessor.dispose();
        }
        turnProcessor = new TurnProcessor(this);
        turnProcessor.turnProcessingFinished.add(new Listener<Object>() {
            @Override
            public void receive(Signal<Object> signal, Object object) {
                startNextTurn();
            }
        });
        turnProcessor.turnChanged.add(new Listener<Object>() {
            @Override
            public void receive(Signal<Object> signal, Object object) {
                ui.updateTurnIndicators();
            }
        });
    }

    public void pickEntity(float x, float y) {
        EntityPicker picker = new EntityPicker(entityEngine);
        Entity entity = picker.pickNextEntity(pickLogic.getSelectedEntity(), x, y);
        pickLogic.pick(entity);
    }

    public WorldUi getUi() {
        return ui;
    }

    public GlobalUi getGlobalUi() {
        return globalUi;
    }

    public void setDebug(boolean debug) {
        globalUi.getStage().setDebugAll(debug);
        ui.getStage().setDebugAll(debug);
    }

    public void escape() {
        if (!globalUi.isMainMenuVisible()) {
            showMainMenu();
        } else {
            globalUi.setMainMenuVisible(false);
        }
    }

    private void showMainMenu() {
        globalUi.setMainMenuVisible(true);
        globalUi.setCanSaveGame(isGameInProgress());
    }

    public boolean isGameInProgress() {
        return true;
    }

    public WorldState buildWorldState() {
        return new WorldStateBuilder(this).build();
    }

    public void endTurn() {
        turnProcessor.startProcessing();
        ui.startTurnProcessing();
    }

    private void startNextTurn() {
        ui.endTurnProcessing();
        selectionChanged.dispatch(pickLogic.getSelectedEntity());
    }

    public int getTurn() {
        return world != null ? turnProcessor.getTurn() : 0;
    }

    public void setTurn(int turn) {
        turnProcessor.setTurn(turn);
    }

    public PickLogic getPickLogic() {
        return pickLogic;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public DebugMode getDebugMode() {
        return debugMode;
    }
}
