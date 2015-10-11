package masterofgalaxy.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ColorButton extends Button {
    private Color frameColor = Color.YELLOW;

    public ColorButton(Skin skin) {
        super(skin, "white");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(frameColor);
        if (isChecked()) {
            NinePatch patch = getSkin().getPatch("default-round-white-frame");
            patch.draw(batch, getX(), getY(), getWidth(), getHeight());
        }
    }
}
