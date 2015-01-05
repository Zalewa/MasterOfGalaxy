package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.UiSkin;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ui.ConsumeTouchAdapter;
import masterofgalaxy.world.WorldScreen;

public class WorldUi implements Disposable {
    private Skin skin;
    private Stage stage;
    private WorldScreen worldScreen;
    private EntitySelectionListener entitySelectionListener = new EntitySelectionListener();

    private StarUi starUi;
    private Table mainLayout;
    private TextButton mainMenuButton;

    public WorldUi(WorldScreen worldScreen) {
        this.worldScreen = worldScreen;
        skin = UiSkin.skin;
        stage = new Stage(new ScreenViewport());

        setupMainLayout();
        setupStarUi();
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

    private void setupStarUi() {
        starUi = new StarUi(worldScreen.getGame(), skin);
        starUi.setVisible(false);
        mainLayout.add(starUi).expand().fillX().center().top();
        mainLayout.row();
    }

    private void setupButtons() {
        setupMainMenuButton();
    }

    private void setupMainMenuButton() {
        mainMenuButton = new TextButton("Main Menu", skin);
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                worldScreen.getGlobalUi().setMainMenuVisible(true);
            }
        });
        mainLayout.add(mainMenuButton).expand().fillX().center().bottom();
        mainLayout.row();
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
        if (entity == null) {
            starUi.setVisible(false);
            return;
        }
        if (Mappers.star.has(entity)) {
            starUi.setVisible(true);
            starUi.setEntity(entity);
        }
    }

    public void dispose() {
        worldScreen.selectionChanged.remove(entitySelectionListener);
    }

    public void reset() {
        updateSelectionUi(null);
    }

    private class EntitySelectionListener implements Listener<Entity> {
        @Override
        public void receive(Signal<Entity> signal, Entity object) {
            updateSelectionUi(object);
        }
    }

    private void applyTranslation() {
        mainMenuButton.setText(I18N.i18n.format("$mainMenu"));
    }
}
