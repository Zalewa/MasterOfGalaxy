package masterofgalaxy;

import com.badlogic.gdx.math.MathUtils;

import java.util.Random;

public class RandomUtil {
    public static float nextFloat(Random random, float min, float max) {
        return min + (random.nextFloat() * (max - min));
    }
}
