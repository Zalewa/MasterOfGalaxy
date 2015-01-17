package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationChangedListener;
import masterofgalaxy.ecs.components.*;
import masterofgalaxy.world.StarColonizer;
import masterofgalaxy.world.WorldScreen;

public class FreeStarUi extends Table implements Localizable {
    private Entity entity;
    private TextButton colonizeButton;
    private Skin skin;
    private MogGame game;

    public FreeStarUi(MogGame game, Skin skin) {
        super(skin);
        this.skin = skin;
        this.game = game;
        I18N.localeChanged.add(new LocalizationChangedListener(this));

        pad(5.0f);

        setupUi();
        applyTranslation();
    }

    private void setupUi() {
        colonizeButton = new TextButton("", skin);
        colonizeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                throw new RuntimeException("not implemented yet :(");
            }
        });
        add(colonizeButton).expandX().fillX();
        row();
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        if (entity == null) {
            return;
        }
        colonizeButton.setVisible(canColonize());
    }

    private boolean canColonize() {
        return StarColonizer.canColonize(game.getWorldScreen(), entity);
    }

    @Override
    public void applyTranslation() {
        colonizeButton.setText(I18N.resolve("$colonize"));
    }
}
