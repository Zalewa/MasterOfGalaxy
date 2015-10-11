package masterofgalaxy.mainmenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationChangedListener;
import masterofgalaxy.exceptions.SavedGameException;
import masterofgalaxy.gamestate.savegame.GameState;
import masterofgalaxy.gamestate.savegame.SaveGameStorage;
import masterofgalaxy.mainmenu.options.OptionsMenu;
import masterofgalaxy.ui.ActorRemoveEscapeKeyAdapter;
import masterofgalaxy.ui.Ui;

public class MainMenu extends Window implements Localizable {
    private Skin skin;
    private Container<TextButton> resumeGameButtonContainer;
    private TextButton resumeGameButton;
    private TextButton newGameButton;
    private TextButton optionsButton;
    private TextButton exitButton;
    private Container<TextButton> saveGameButtonContainer;
    private TextButton saveGameButton;
    private Container<TextButton> loadGameButtonContainer;
    private TextButton loadGameButton;
    private Container<TextButton> cancelButtonContainer;
    private TextButton cancelButton;
    private Container<TextButton> titleScreenButtonContainer;
    private TextButton titleScreenButton;
    private Button xButton;
    private Table mainLayout;
    private MogGame game;
    private ActorRemoveEscapeKeyAdapter escapeAdapter = new ActorRemoveEscapeKeyAdapter(this);

    public MainMenu(MogGame game, Skin skin) {
        super("title", skin);
        I18N.localeChanged.add(new LocalizationChangedListener(this));
        this.game = game;
        this.skin = skin;
        setModal(true);
        addListener(escapeAdapter);

        mainLayout = new Table(skin);
        mainLayout.pad(12.0f);
        mainLayout.defaults().space(5.0f);
        add(mainLayout).expand().fill();

        setupXButton();
        setupResumeGameButton();
        setupNewGameButton();
        setupSaveGameButton();
        setupLoadGameButton();
        setupOptionsButton();
        setupTitleScreenButton();
        setupExitButton();
        setupCancelButton();

        applyTranslation();
    }

    private void setupXButton() {
        xButton = new TextButton("X", skin);
        xButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                remove();
            }
        });

        getTitleTable().add(xButton).height(getPadTop());
    }

    private void setupResumeGameButton() {
        resumeGameButton = new TextButton("$resumeGame", skin);
        resumeGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.resumeGame();
            }
        });

        resumeGameButtonContainer = new Container<TextButton>(resumeGameButton);
        resumeGameButtonContainer.fillX();
        mainLayout.add(resumeGameButtonContainer).fillX();
        mainLayout.row();
    }

    private void setupNewGameButton() {
        newGameButton = new TextButton("$newGame", skin);
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.startNewGame();
            }
        });

        mainLayout.add(newGameButton).fillX();
        mainLayout.row();
    }

    private void setupSaveGameButton() {
        saveGameButton = new TextButton("$saveGame", skin);
        saveGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveGame();
            }
        });

        saveGameButtonContainer = new Container<TextButton>(saveGameButton);
        saveGameButtonContainer.fillX();
        mainLayout.add(saveGameButtonContainer).fillX();
        mainLayout.row();
    }

    private void setupLoadGameButton() {
        loadGameButton = new TextButton("$loadGame", skin);
        loadGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadGame();
            }
        });

        loadGameButtonContainer = new Container<TextButton>(loadGameButton);
        loadGameButtonContainer.fillX();
        mainLayout.add(loadGameButtonContainer).fillX();
        mainLayout.row();
    }

    private void setupOptionsButton() {
        optionsButton = new TextButton("Options", skin);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                OptionsMenu menu = new OptionsMenu(game, skin);
                getParent().addActor(menu);
                getStage().setKeyboardFocus(menu);
                Ui.centerWithinStage(menu);
            }
        });

        mainLayout.add(optionsButton).fillX();
        mainLayout.row();
    }

    private void setupTitleScreenButton() {
        titleScreenButton = new TextButton("$titleScreen", skin);
        titleScreenButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToTitleScreen();
            }
        });

        titleScreenButtonContainer = new Container<TextButton>(titleScreenButton);
        titleScreenButtonContainer.fillX();
        mainLayout.add(titleScreenButtonContainer).fillX();
        mainLayout.row();
    }

    private void setupExitButton() {
        exitButton = new TextButton("Exit Game", skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.exit();
            }
        });

        mainLayout.add(exitButton).expandY().uniformX().fillX().top();
        mainLayout.row();
    }

    private void setupCancelButton() {
        cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                remove();
            }
        });

        cancelButtonContainer = new Container<TextButton>(cancelButton);
        cancelButtonContainer.fillX();
        mainLayout.add(cancelButtonContainer).bottom();
        mainLayout.row();
    }

    public void setCancelable(boolean cancelable) {
        escapeAdapter.setEnabled(cancelable);
        xButton.setVisible(cancelable);
        if (cancelable) {
            cancelButtonContainer.setActor(cancelButton);
        } else {
            cancelButtonContainer.removeActor(cancelButton);
        }
    }

    public void setCanGoToTitleScreen(boolean can) {
        if (can) {
            titleScreenButtonContainer.setActor(titleScreenButton);
        } else {
            titleScreenButtonContainer.removeActor(titleScreenButton);
        }
    }

    public void setCanSaveGame(boolean can) {
        if (can) {
            saveGameButtonContainer.setActor(saveGameButton);
        } else {
            saveGameButtonContainer.removeActor(saveGameButton);
        }
    }

    public void setCanResumeGame(boolean can) {
        if (can) {
            resumeGameButtonContainer.setActor(resumeGameButton);
        } else {
            resumeGameButtonContainer.removeActor(resumeGameButton);
        }
    }

    private void saveGame() {
        try {
            new SaveGameStorage().save(game, "mog.save");
        } catch (SavedGameException e) {
            e.printStackTrace();
            new Dialog(I18N.resolve("$saveError"), skin).text(e.getLocalizedMessage()).button(I18N.resolve("$close")).show(getStage());
        }
    }

    private void loadGame() {
        try {
            GameState state = new SaveGameStorage().load("mog.save");
            game.restoreGame(state);
        } catch (SavedGameException e) {
            e.printStackTrace();
            new Dialog(I18N.resolve("$loadError"), skin).text(e.getLocalizedMessage()).button(I18N.resolve("$close")).show(getStage());
        }
    }

    @Override
    public void applyTranslation() {
        getTitleLabel().setText(I18N.resolve("$mainMenu"));
        resumeGameButton.setText(I18N.resolve("$resumeGame"));
        newGameButton.setText(I18N.resolve("$newGame"));
        saveGameButton.setText(I18N.resolve("$saveGame"));
        loadGameButton.setText(I18N.resolve("$loadGame"));
        optionsButton.setText(I18N.resolve("$options"));
        titleScreenButton.setText(I18N.resolve("$titleScreen"));
        exitButton.setText(I18N.resolve("$exitGame"));
        cancelButton.setText(I18N.resolve("$cancel"));
        pack();
    }
}
