package masterofgalaxy.world.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.assets.i18n.LocalizationChangedListener;
import masterofgalaxy.ecs.components.Mappers;
import masterofgalaxy.ecs.components.NameComponent;
import masterofgalaxy.ecs.components.PlayerOwnerComponent;
import masterofgalaxy.ecs.components.StarComponent;

public class StarUi extends Table implements Localizable {
    private Skin skin;
    private Entity entity;
    private Label nameLabel;
    private Label ownerLabel;
    private Label starLabel;
    private Table starLayout;
    private Table starInfoLayout;
    private Label planetLabel;
    private Image planetImage;
    private MogGame game;

    public StarUi(MogGame game, Skin skin) {
        super(skin);
        this.game = game;
        this.skin = skin;
        I18N.localeChanged.add(new LocalizationChangedListener(this));

        setupHeader();
        setupStarInfo();

        setBackground("default-rect");
        setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void setupHeader() {
        Table table = new Table(skin);

        nameLabel = new Label("", skin);
        table.add(nameLabel).expandX();
        table.row();

        ownerLabel = new Label("", skin);
        table.add(ownerLabel).expandX();

        add(table).expandX().fill();
        row();
    }

    private void setupStarInfo() {
        starLayout = new Table(skin);
        starLayout.setBackground("black");
        //starLayout.setColor();
        add(starLayout).expandX().fillX().pad(5.0f);
        row();

        planetImage = new Image();
        starLayout.add(planetImage).left().width(64.0f).height(64.0f);

        starInfoLayout = new Table(skin);
        starLayout.add(starInfoLayout).expand().fillX().top();

        starLabel = new Label("", skin);
        starInfoLayout.add(starLabel).expandX().left();

        starInfoLayout.row();
        planetLabel = new Label("", skin);
        starInfoLayout.add(planetLabel).expandX().left();
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        if (entity == null) {
            return;
        }
        NameComponent name = Mappers.name.get(entity);
        PlayerOwnerComponent owner = Mappers.playerOwner.get(entity);
        StarComponent starComponent = Mappers.star.get(entity);
        if (name != null) {
            nameLabel.setText(name.getName());
            ownerLabel.setColor(owner.getOwner().getColor());
            if (owner.getOwner().isValid()) {
                ownerLabel.setText(owner.getOwner().getName());
            } else {
                ownerLabel.setText("");
            }
            starLabel.setText(starComponent.klass.getLocalizedName());
            planetLabel.setText(starComponent.planetKlass.getLocalizedName());
            planetImage.setDrawable(getPlanetDrawable(starComponent));
        }
    }

    private TextureRegionDrawable getPlanetDrawable(StarComponent starComponent) {
        Texture texture;
        try {
            texture = game.getAssetManager().get(starComponent.planetKlass.getTextureName(), Texture.class);
        } catch (GdxRuntimeException e) {
            return null;
        }
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    @Override
    public void applyTranslation() {
        setEntity(entity);
    }
}
