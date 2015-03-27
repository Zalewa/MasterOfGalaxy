package masterofgalaxy.gamestate;

import com.badlogic.gdx.utils.Array;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.actors.ShipModuleType;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.gamestate.ships.ShipDesign;

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

    public PlayerBuilder(MogGame game, long seed) {
        this.game = game;
        random = new Random(seed);
    }

    public Array<Player> randomizePlayers(int count) {
        List<PlayerColor> colors = shufflePlayerColors();
        List<Race> races = shuffleRaces();

        if (colors.size() < count) {
            throw new IllegalArgumentException("player count cannot be greater than available colors count");
        }

        Array<Player> result = new Array<Player>();
        for (int i = 0; i < count; ++i) {
            Player player = new Player();
            player.setName("Player-" + i);
            player.setPlayerColor(colors.get(i));
            player.setRace(races.get(i % races.size()));
            createStartingShipDesigns(player);
            player.getState().setTechTree(game.getActorAssets().tech);
            result.add(player);
        }

        return result;
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
            colors.add(color);
        }
        Collections.shuffle(colors, random);
        return colors;
    }
}
