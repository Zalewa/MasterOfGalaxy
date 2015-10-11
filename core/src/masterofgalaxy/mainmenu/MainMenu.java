package masterofgalaxy.mainmenu;

import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.exceptions.SavedGameException;
import masterofgalaxy.gamestate.savegame.GameState;
import masterofgalaxy.gamestate.savegame.SaveGameStorage;
import masterofgalaxy.mainmenu.newgame.NewGameWizard;
import masterofgalaxy.mainmenu.options.OptionsMenu;
import masterofgalaxy.ui.WindowExtender;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenu extends Window implements Localizable {
    private WindowExtender ex;
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
    private Table mainLayout;
    private MogGame game;

    public MainMenu(MogGame game, Skin skin) {
        super("title", skin);
        ex = new WindowExtender(this);
        this.game = game;
        setModal(true);

        mainLayout = new Table(skin);
        mainLayout.pad(12.0f);
        mainLayout.defaults().space(5.0f);
        add(mainLayout).expand().fill();

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


    private void setupResumeGameButton() {
        resumeGameButton = new TextButton("$resumeGame", getSkin());
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
        newGameButton = new TextButton("$newGame", getSkin());
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                NewGameWizard newGameWizard = new NewGameWizard(game, getSkin());
                newGameWizard.ex.setDestroyOnClose(true);
                newGameWizard.ex.closedSignal.add(new Listener<Object>() {
                    @Override
                    public void receive(Signal<Object> signal, Object object) {
                        setVisible(true);
                    }
                });
                newGameWizard.ex.show(getStage());
                setVisible(false);
            }
        });

        mainLayout.add(newGameButton).fillX();
        mainLayout.row();
    }

    private void setupSaveGameButton() {
        saveGameButton = new TextButton("$saveGame", getSkin());
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
        loadGameButton = new TextButton("$loadGame", getSkin());
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
        optionsButton = new TextButton("Options", getSkin());
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                OptionsMenu menu = new OptionsMenu(game, getSkin());
                menu.ex.show(getStage());
            }
        });

        mainLayout.add(optionsButton).fillX();
        mainLayout.row();
    }

    private void setupTitleScreenButton() {
        titleScreenButton = new TextButton("$titleScreen", getSkin());
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
        exitButton = new TextButton("Exit Game", getSkin());
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
        cancelButton = new TextButton("Cancel", getSkin());
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
        ex.setCloseable(cancelable);
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
            new Dialog(I18N.resolve("$saveError"), getSkin()).text(e.getLocalizedMessage()).button(I18N.resolve("$close")).show(getStage());
        }
    }

    private void loadGame() {
        try {
            GameState state = new SaveGameStorage().load("mog.save");
            game.restoreGame(state);
        } catch (SavedGameException e) {
            e.printStackTrace();
            new Dialog(I18N.resolve("$loadError"), getSkin()).text(e.getLocalizedMessage()).button(I18N.resolve("$close")).show(getStage());
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
