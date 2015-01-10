package masterofgalaxy;

import java.util.Random;

public class RandomUtil {
    public static float nextFloat(Random random, float min, float max) {
        return min + (random.nextFloat() * (max - min));
    }

    public static int nextSign(Random random) {
        return random.nextInt(1) == 1 ? 1 : -1;
    }
}
