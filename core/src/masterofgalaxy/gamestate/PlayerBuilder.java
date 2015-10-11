package masterofgalaxy.gamestate;

import com.badlogic.gdx.utils.Array;

import masterofgalaxy.MogGame;
import masterofgalaxy.assets.actors.ShipModuleType;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.gamestate.ships.ShipDesign;
import masterofgalaxy.world.worldbuild.PlayerSetup;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PlayerBuilder {
    public static Array<Player> fromStates(Array<PlayerState> states) {
        Array<Player> result = new Array<Player>();
        for (PlayerState state : states) {
            result.add(new Player(state));
        }
        return result;
    }

    private Random random;
    private MogGame game;
    private Array<PlayerSetup> predefinedPlayers;
    private int numRandomPlayers;

    public PlayerBuilder(MogGame game, long seed) {
        this.game = game;
        random = new Random(seed);
    }

    public Array<Player> build() {
        Array<Player> players = new Array<Player>();
        players.addAll(buildPredefinedPlayers());
        players.addAll(randomizePlayers());
        return players;
    }

    private Array<Player> buildPredefinedPlayers() {
        Array<Player> players = new Array<Player>();
        for (PlayerSetup setup : predefinedPlayers) {
            Player player = mkNewPlayer();
            player.setName(setup.getName());
            player.setPlayerColor(setup.getColor());
            player.setRace(setup.getRace());
            players.add(player);
        }
        return players;
    }

    private Array<Player> randomizePlayers() {
        List<PlayerColor> colors = shufflePlayerColors();
        List<Race> races = shuffleRaces();

        if (colors.size() < numRandomPlayers) {
            throw new IllegalArgumentException("player count cannot be greater than available colors count");
        }

        Array<Player> result = new Array<Player>();
        for (int i = 0; i < numRandomPlayers; ++i) {
            Player player = mkNewPlayer();
            player.setName("Player-" + (i + predefinedPlayers.size));
            player.setPlayerColor(colors.get(i));
            player.setRace(races.get(i % races.size()));
            result.add(player);
        }

        return result;
    }

    private Player mkNewPlayer() {
        Player player = new Player();
        createStartingShipDesigns(player);
        player.getState().setTechTree(game.getActorAssets().tech);
        return player;
    }

    private void createStartingShipDesigns(Player player) {
        player.getState().getShipDesigns().add(createScout());
        player.getState().getShipDesigns().add(createColonyShip());
    }

    private ShipDesign createScout() {
        ShipDesign design = new ShipDesign();
        design.setName(I18N.resolve("$scoutShip"));
        design.setHull(game.getActorAssets().ships.findHull("tiny"));
        design.addModule(game.getActorAssets().ships.findModule(ShipModuleType.Engine, "nuclear"));
        design.addModule(game.getActorAssets().ships.findModule(ShipModuleType.Special, "fuelTank"));
        return design;
    }

    private ShipDesign createColonyShip() {
        ShipDesign design = new ShipDesign();
        design.setName(I18N.resolve("$colonyShip"));
        design.setHull(game.getActorAssets().ships.findHull("large"));
        design.addModule(game.getActorAssets().ships.findModule(ShipModuleType.Engine, "nuclear"));
        design.addModule(game.getActorAssets().ships.findModule(ShipModuleType.Special, "colony"));
        return design;
    }

    private List<Race> shuffleRaces() {
        List<Race> races = new LinkedList<Race>();
        for (Race race : game.getActorAssets().races.races) {
            races.add(race);
        }
        Collections.shuffle(races, random);
        return races;
    }

    private List<PlayerColor> shufflePlayerColors() {
        List<PlayerColor> colors = new LinkedList<PlayerColor>();
        for (PlayerColor color : game.getActorAssets().playerColors.colors) {
            if (!isColorUsedByPredefinedPlayer(color)) {
                colors.add(color);
            }
        }
        Collections.shuffle(colors, random);
        return colors;
    }

    private boolean isColorUsedByPredefinedPlayer(PlayerColor color) {
        for (PlayerSetup setup : predefinedPlayers) {
            if (setup.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }

    public void setPredefinedPlayers(Array<PlayerSetup> predefinedPlayers) {
        this.predefinedPlayers = predefinedPlayers;
    }

    public void setNumRandomPlayers(int numRandomPlayers) {
        this.numRandomPlayers = numRandomPlayers;
    }
}
