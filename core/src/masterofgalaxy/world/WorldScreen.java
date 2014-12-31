package masterofgalaxy.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import masterofgalaxy.MogGame;
import masterofgalaxy.ecs.EntityPicker;
import masterofgalaxy.ecs.systems.*;
import masterofgalaxy.world.ui.GlobalUi;
import masterofgalaxy.world.ui.WorldUi;

public class WorldScreen extends ScreenAdapter {
    private MogGame game;
    private PooledEngine entityEngine;
    private WorldCamera camera;
    private World world;
    private WorldUi ui;
    private GlobalUi globalUi;
    private WorldInputProcessor inputProcessor =  new WorldInputProcessor(this);
    private SelectionTracker selection = null;
    private ExtendViewport viewport;
    private InputMultiplexer inputMultiplexer = null;
    private Listener<Entity> selectionChangedListener;

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
        camera = new WorldCamera(this);
        viewport = new ExtendViewport(1000.0f, 1000.0f, camera.getCamera());

        entityEngine = new PooledEngine();
        entityEngine.addSystem(new BlinkSystem());
        entityEngine.addSystem(new SelectionScalingSystem());
        entityEngine.addSystem(new ParentshipSystem());
        entityEngine.addSystem(new RenderingSystem(game));
        entityEngine.addSystem(new TextRenderingSystem(game));
    }

    private void createNewWorld() {
        WorldBuilder builder = new WorldBuilder(this, System.currentTimeMillis());
        world = builder.build();

        if (selection != null) {
            selection.dispose();
        }
        selection = new SelectionTracker(game, entityEngine);
        selection.selectionChanged.add(selectionChangedListener);

        viewport.setMinWorldWidth(world.getPlayField().getWidth());
        viewport.setMinWorldHeight(world.getPlayField().getHeight());
        camera.centerCamera();
    }

    @Override
    public void render(float delta) {
        renderEngine(delta);
        renderUi(delta);
        renderGlobalUi(delta);
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

    public void resetGame() {
        if (world != null) {
            world.dispose();
        }
        ui.reset();
        resetEntityEngine();
        createNewWorld();
    }

    private void resetEntityEngine() {
        entityEngine.removeAllEntities();
        entityEngine.clearPools();
    }

    public void pickEntity(float x, float y) {
        EntityPicker picker = new EntityPicker(entityEngine);
        Entity entity = picker.pickNextEntity(selection.getSelectedEntity(), x, y);
        selection.setSelection(entity);
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
        if (globalUi.isMainMenuVisible()) {
            globalUi.setMainMenuVisible(false);
        } else {
            globalUi.setMainMenuVisible(true);
        }
    }
}
