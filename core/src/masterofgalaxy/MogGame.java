package masterofgalaxy;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import masterofgalaxy.assets.fonts.Font;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.Sprite;
import masterofgalaxy.assets.UiSkin;
import masterofgalaxy.assets.actors.ActorAssets;
import masterofgalaxy.config.GeneralConfig;
import masterofgalaxy.config.VideoConfig;
import masterofgalaxy.config.VideoDisplayMode;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.titlescreen.TitleScreen;
import masterofgalaxy.world.WorldScreen;

import java.util.Locale;

public class MogGame extends Game {
	SpriteBatch spriteBatch;
	Mappers componentMappers;
	TitleScreen titleScreen;
	WorldScreen worldScreen;
	AssetManager assetManager;
	InputMultiplexer inputMultiplexer;
	ActorAssets actorAssets;

	@Override
	public void create () {
		setupScreenMode();

		assetManager = new AssetManager();
		spriteBatch = new SpriteBatch();
		setupInput();
		createEntityEngine();

		loadAssets();

		goToTitleScreen();
	}

	public void goToTitleScreen() {
		if (titleScreen == null) {
			titleScreen = new TitleScreen(this);
		}
		titleScreen.reset();
		setScreen(titleScreen);
	}

	public void startNewGame() {
		if (worldScreen == null) {
			worldScreen = new WorldScreen(this);
		}
		worldScreen.resetGame();
		setScreen(worldScreen);
	}

	public void resumeGame() {
		if (isGameResumable()) {
			setScreen(worldScreen);
		}
	}

	public boolean isGameResumable() {
		return worldScreen != null && worldScreen.isGameInProgress();
	}

	private void setupScreenMode() {
		VideoConfig config = new VideoConfig();
		VideoDisplayMode mode = config.getLastDisplayMode();
		Gdx.graphics.setDisplayMode(mode.width, mode.height, mode.isFullscreen());
		if (!config.hasDisplayModeStored(VideoDisplayMode.getCurrentScreenMode())) {
			config.storeDisplayMode(VideoDisplayMode.getCurrentVideoDisplayMode());
			config.flush();
		}
	}

	private void setupInput() {
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(new GlobalInput(this));
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	private void loadAssets() {
		loadI18nAssets();
		Font.loadAll(assetManager);
		assetManager.finishLoading();

		Sprite.loadAll(assetManager);
		UiSkin.loadAll(assetManager);

		actorAssets = new ActorAssets();
		actorAssets.planetClasses.load(Gdx.files.internal("actors/planets.json"));
		actorAssets.starClasses.load(Gdx.files.internal("actors/stars.json"));
		actorAssets.loadAssets(assetManager);

		assetManager.finishLoading();

		actorAssets.applyAssets(assetManager);
	}

	private void loadI18nAssets() {
		I18N.loadLocalizations(Gdx.files.internal("i18n/l10n.json"));

		GeneralConfig config = new GeneralConfig();
		Locale locale = config.getLocale();
		I18N.load(locale);
		config.setLocale(locale);
		config.flush();
	}

	private void createEntityEngine() {
		componentMappers = new Mappers();
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}

	@Override
	public void dispose() {
		saveCurrentVideoMode();
		if (titleScreen != null) {
			titleScreen.dispose();
		}
		if (worldScreen != null) {
			worldScreen.dispose();
		}
		assetManager.dispose();
	}

	private void saveCurrentVideoMode() {
		VideoConfig config = new VideoConfig();
		config.storeDisplayMode(VideoDisplayMode.getCurrentVideoDisplayMode());
		config.flush();
	}

	public Mappers getComponentMappers() {
		return componentMappers;
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public InputMultiplexer getInputMultiplexer() {
		return inputMultiplexer;
	}

	public ActorAssets getActorAssets() {
		return actorAssets;
	}

	public void toggleFullscreen() {
		if (Gdx.graphics.isFullscreen()) {
			setWindowed();
		} else {
			setFullscreen();
		}
	}

	private void setWindowed() {
		VideoConfig config = new VideoConfig();
		VideoDisplayMode mode = config.getDisplayModeForScreenMode(VideoDisplayMode.ScreenMode.Windowed);
		Gdx.graphics.setDisplayMode(mode.width, mode.height, false);
	}

	private void setFullscreen() {
		VideoConfig config = new VideoConfig();
		VideoDisplayMode mode = config.getDisplayModeForScreenMode(VideoDisplayMode.ScreenMode.Fullscreen);
		Gdx.graphics.setDisplayMode(mode.width, mode.height, true);
	}

	public void exit() {
		dispose();
		Gdx.app.exit();
	}
}
