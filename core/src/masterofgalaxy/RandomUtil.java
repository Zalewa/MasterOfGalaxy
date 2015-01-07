package masterofgalaxy;

import java.util.Random;

public class RandomUtil {
    public static float nextFloat(Random random, float min, float max) {
        return min + (random.nextFloat() * (max - min));
    }
}
