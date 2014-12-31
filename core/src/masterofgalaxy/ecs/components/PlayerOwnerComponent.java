package masterofgalaxy.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;
import masterofgalaxy.gamestate.Player;

public class PlayerOwnerComponent extends Component implements Pool.Poolable {
    private Player owner = Player.nullPlayer;

    private Listener<Color> colorChangeListener = new Listener<Color>() {
        @Override
        public void receive(Signal<Color> signal, Color color) {
            ownerColorChanged.dispatch(color);
        }
    };

    public Signal<Color> ownerColorChanged = new Signal<Color>();

    @Override
    public void reset() {
        ownerColorChanged.removeAllListeners();
        setOwner(null);
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player player) {
        if (player == null) {
            player = Player.nullPlayer;
        }
        if (owner != null) {
            owner.colorChanged.remove(colorChangeListener);
        }
        owner = player;
        owner.colorChanged.add(colorChangeListener);
        ownerColorChanged.dispatch(owner.getColor());
    }
}
