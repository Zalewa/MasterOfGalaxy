package masterofgalaxy.world.stars;

import com.badlogic.gdx.math.MathUtils;
import masterofgalaxy.AskForFloat;

class Resource {
    private float amount;

    public boolean locked;
    public AskForFloat minAmountAsker;

    public Resource() {
    }

    public void setValues(Resource other) {
        locked = other.locked;
        amount = other.amount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float value) {
        float minAmount = 0.0f;
        if (minAmountAsker != null) {
            minAmount = minAmountAsker.ask();
        }
        amount = MathUtils.clamp(value, minAmount, MainResourceDistribution.pool);
    }
}
