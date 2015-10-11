package masterofgalaxy.mainmenu.newgame;

import masterofgalaxy.MogGame;
import masterofgalaxy.Strings;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.gamestate.PlayerColor;
import masterofgalaxy.gamestate.Race;
import masterofgalaxy.ui.ColorButton;
import masterofgalaxy.ui.Ui;
import masterofgalaxy.ui.WindowExtender;
import masterofgalaxy.world.worldbuild.GameStartSetup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter.DigitsOnlyFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class NewGameWizard extends Window implements Localizable {
    public WindowExtender ex;

    private MogGame game;
    private Table mainLayout;
    private Label nameLabel;
    private TextField nameInput;
    private Label raceLabel;
    private SelectBox<Race> raceSelectBox;
    private Label worldSizeLabel;
    private TextButton smallWorldButton;
    private TextButton mediumWorldButton;
    private TextButton bigWorldButton;
    private Label parsecsLabel;
    private TextField parsecsInput;
    private Label colorLabel;
    private Label rivalsLabel;
    private TextField rivalsInput;
    private TextButton startButton;
    private ButtonGroup<Button> colorButtons;

    public NewGameWizard(MogGame game, Skin skin) {
        super("newgamewizard", skin);
        this.game = game;
        ex = new WindowExtender(this);
        setup();
    }

    private void setup() {
        setupMainLayout();
        setupName();
        setupRace();
        setupColors();
        setupWorldSize();
        setupRivals();
        setupStart();
        pack();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Ui.centerWithinStage(NewGameWizard.this);
            }
        });
    }

    private void setupMainLayout() {
        mainLayout = new Table(getSkin());
        mainLayout.pad(12.0f);
        mainLayout.defaults().space(5.0f).fillX();
        add(mainLayout);
    }

    private void setupName() {
        nameLabel = new Label("", getSkin());
        mainLayout.add(nameLabel);

        nameInput = new TextField("", getSkin());
        mainLayout.add(nameInput);
        mainLayout.row();
    }

    private void setupRace() {
        raceLabel = new Label("", getSkin());
        mainLayout.add(raceLabel);

        raceSelectBox = new SelectBox<Race>(getSkin());
        raceSelectBox.setItems(game.getActorAssets().races.races);
        mainLayout.add(raceSelectBox);
        mainLayout.row();
    }

    private void setupColors() {
        colorLabel = new Label("", getSkin());
        mainLayout.add(colorLabel);

        Table table = new Table(getSkin());
        colorButtons = new ButtonGroup<Button>();
        for (PlayerColor color : game.getActorAssets().playerColors.colors) {
            Button button = new ColorButton(getSkin());
            button.setColor(color.getColor());
            button.setName(color.getName());
            table.add(button).width(24.0f).height(24.0f);
            colorButtons.add(button);
        }
        mainLayout.add(table);
        mainLayout.row();
    }

    private void setupWorldSize() {
        worldSizeLabel = new Label("", getSkin());
        mainLayout.add(worldSizeLabel);

        Table tableButtons = new Table(getSkin());
        smallWorldButton = new TextButton("", getSkin());
        smallWorldButton.addCaptureListener(new ParsecSelectionListener(
                GameStartSetup.SMALL_WORLD));
        tableButtons.add(smallWorldButton);
        mediumWorldButton = new TextButton("", getSkin());
        mediumWorldButton.addCaptureListener(new ParsecSelectionListener(
                GameStartSetup.MEDIUM_WORLD));
        tableButtons.add(mediumWorldButton);
        bigWorldButton = new TextButton("", getSkin());
        bigWorldButton.addCaptureListener(new ParsecSelectionListener(
                GameStartSetup.LARGE_WORLD));
        tableButtons.add(bigWorldButton);

        mainLayout.add(tableButtons);
        mainLayout.row();

        parsecsLabel = new Label("", getSkin());
        mainLayout.add(parsecsLabel);

        parsecsInput = new TextField("", getSkin());
        parsecsInput.setTextFieldFilter(new DigitsOnlyFilter());
        parsecsInput.setText(String.valueOf(GameStartSetup.MEDIUM_WORLD));
        mainLayout.add(parsecsInput);

        mainLayout.row();
    }

    private void setupRivals() {
        rivalsLabel = new Label("", getSkin());
        mainLayout.add(rivalsLabel);

        rivalsInput = new TextField(String.valueOf(3), getSkin());
        rivalsInput.setTextFieldFilter(new DigitsOnlyFilter());
        mainLayout.add(rivalsInput);
        mainLayout.row();
    }

    private void setupStart() {
        startButton = new TextButton("", getSkin());
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startGame();
            }
        });
        mainLayout.add(startButton).colspan(2);
        mainLayout.row();
    }

    private void startGame() {
        GameStartSetup setup = mkGameStartSetup();
        if (!Strings.hasContents(setup.getPlayerName())) {
            showError(I18N.resolve("$inputPlayerNameError"));
            return;
        }
        if (setup.getNumRivals() < GameStartSetup.MIN_RIVALS
                || setup.getNumRivals() > GameStartSetup.MAX_RIVALS) {
            showError(I18N.resolve("$inputRivalsRangeError",
                    GameStartSetup.MIN_RIVALS, GameStartSetup.MAX_RIVALS));
            return;
        }
        if (setup.getWorldSize() < GameStartSetup.SMALL_WORLD
                || setup.getWorldSize() > GameStartSetup.LARGE_WORLD) {
            showError(I18N.resolve("$inputWorldSizeError",
                    GameStartSetup.SMALL_WORLD, GameStartSetup.LARGE_WORLD));
            return;
        }
        game.startNewGame(setup);
        ex.close();
    }

    private void showError(String error) {
        new Dialog(I18N.resolve("$newGame"), getSkin()).text(error)
                .button(I18N.resolve("$close")).show(getStage());
    }

    private GameStartSetup mkGameStartSetup() {
        GameStartSetup setup = new GameStartSetup();
        setup.setNumRivals(Integer.valueOf(rivalsInput.getText()));
        setup.setPlayerColor(game.getActorAssets().playerColors
                .findColor(colorButtons.getChecked().getColor()));
        setup.setPlayerName(nameInput.getText());
        setup.setPlayerRace(raceSelectBox.getSelected());
        setup.setWorldSize(Integer.valueOf(parsecsInput.getText()));
        return setup;
    }


    @Override
    public void applyTranslation() {
        getTitleLabel().setText(I18N.resolve("$newGame"));
        nameLabel.setText(I18N.resolve("$inputName"));
        raceLabel.setText(I18N.resolve("$inputRace"));
        colorLabel.setText(I18N.resolve("$inputColor"));
        worldSizeLabel.setText(I18N.resolve("$inputWorldSize"));
        smallWorldButton.setText(I18N.resolve("$smallWorld"));
        mediumWorldButton.setText(I18N.resolve("$mediumWorld"));
        bigWorldButton.setText(I18N.resolve("$largeWorld"));
        parsecsLabel.setText(I18N.resolve("$inputParsecs"));
        rivalsLabel.setText(I18N.resolve("$inputNumRivals"));
        startButton.setText(I18N.resolve("$start"));
        pack();
    }

    private class ParsecSelectionListener extends ChangeListener {
        private int parsecs;

        ParsecSelectionListener(int parsecs) {
            this.parsecs = parsecs;
        }

        @Override
        public void changed(ChangeEvent event, Actor actor) {
            parsecsInput.setText(String.valueOf(parsecs));
        }
    }
}
