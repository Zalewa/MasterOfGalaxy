package masterofgalaxy.world.worldbuild;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.gamestate.Player;
import masterofgalaxy.world.World;
import masterofgalaxy.world.WorldScreen;

import java.util.Random;

public class RectangleWorldStarLayout {
    private final float rectPadding = 32.0f;

    private WorldScreen screen;
    private World world;
    private Array<FreeRect> rects;
    private Array<FreeRect> freeRects;
    private Random random;
    private long seed;
    private int numUnownedStars;

    public RectangleWorldStarLayout(WorldScreen screen) {
        this.screen = screen;
    }

    public World buildStars() {
        random = new Random(seed);
        rects = dividePlayfield();
        freeRects = new Array<FreeRect>(rects);

        placePlayers();
        placeUnownedStars();

        return world;
    }

    private void placePlayers() {
        StarBuilder builder = new StarBuilder(screen, world, random);

        for (int i = 0; i < world.getPlayers().size; ++i) {
            Player player = world.getPlayers().get(i);

            Rectangle rect = popNextRectangle();
            Vector2 pos = randomizeStarPositionInRect(rect);
            builder.createHomeworld(player, pos.x, pos.y);
        }
    }

    private void placeUnownedStars() {
        StarBuilder builder = new StarBuilder(screen, world, random);

        for (int i = 0; i < numUnownedStars; ++i) {
            Rectangle rect = popNextRectangle();
            Vector2 pos = randomizeStarPositionInRect(rect);
            builder.createRandomStar(screen.getGame().getActorAssets().starClasses.pickRandrom(random), pos.x, pos.y);
        }
    }

    private Vector2 randomizeStarPositionInRect(Rectangle rect) {
        float offsetX = (rect.width - (rectPadding * 2.0f)) * random.nextFloat();
        float offsetY = (rect.height - (rectPadding * 2.0f)) * random.nextFloat();
        float x = (rect.x + rectPadding) + offsetX;
        float y = (rect.y + rectPadding) + offsetY;
        return new Vector2(x, y);
    }

    private Array<FreeRect> dividePlayfield() {
        Rectangle[] rects = buildSubRects();
        Array<FreeRect> result = new Array<FreeRect>();
        for (int i = 0; i < rects.length; ++i) {
            result.add(new FreeRect(rects[i]));
        }
        return result;
    }

    private Rectangle[] buildSubRects() {
        int numCols = getNumCols();
        int numRows = getNumRows();
        float width = world.getPlayField().width / numCols;
        float height = world.getPlayField().height / numRows;
        Rectangle[] rects = new Rectangle[numCols * numRows];
        for (int col = 0; col < numCols; ++col) {
            for (int row = 0; row < numRows; ++row) {
                Rectangle rect = new Rectangle();
                rect.x = width * col;
                rect.y = height * row;
                rect.width = width;
                rect.height = height;
                rects[index(col, row)] = rect;
            }
        }
        return rects;
    }

    private Rectangle popNextRectangle() {
        int idx = random.nextInt(freeRects.size);
        try {
            return freeRects.get(idx).rect;
        } finally {
            freeRects.removeIndex(idx);
        }
    }

    private int index(int col, int row) {
        return row * getNumCols() + col;
    }

    private int getNumRows() {
        return MathUtils.ceil((float)Math.sqrt(getTotalNumberOfStars()));
    }

    private int getNumCols() {
        return MathUtils.ceil((float)Math.sqrt(getTotalNumberOfStars()));
    }

    private int getTotalNumberOfStars() {
        return numUnownedStars + world.getPlayers().size;
    }

    public int getNumUnownedStars() {
        return numUnownedStars;
    }

    public void setNumUnownedStars(int numUnownedStars) {
        this.numUnownedStars = numUnownedStars;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    private class FreeRect {
        public Rectangle rect;

        public FreeRect(Rectangle rect) {
            this.rect = rect;
        }
    }
}
