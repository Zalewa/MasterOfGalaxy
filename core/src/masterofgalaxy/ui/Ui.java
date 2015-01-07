package masterofgalaxy.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ui {
    public static void centerWithinStage(Actor widget) {
        if (widget.getParent() != null) {
            widget.setPosition(Math.round((widget.getStage().getWidth() - widget.getWidth()) / 2),
                    Math.round((widget.getStage().getHeight() - widget.getHeight()) / 2));
        }
    }
}
