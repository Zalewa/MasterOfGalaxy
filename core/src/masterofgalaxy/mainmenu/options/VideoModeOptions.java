package masterofgalaxy.mainmenu.options;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.assets.i18n.Localizable;
import masterofgalaxy.config.VideoConfig;
import masterofgalaxy.config.VideoDisplayMode;
import masterofgalaxy.config.VideoDisplayMode.ScreenMode;

public class VideoModeOptions extends Table implements Localizable {
    private CheckBox fullscreenCheckbox;
    private Label resolutionsLabel;
    private SelectBox<Resolution> resolutionsSelectBox;
    private MogGame game;

    public VideoModeOptions(MogGame game, Skin skin) {
        super(skin);
        this.game = game;

        setupUi(skin);
        loadResolutions();
        tryToSetCurrentResolution();

        applyTranslation();
    }

    private void setupUi(Skin skin) {
        defaults().left().space(10.0f);

        resolutionsLabel = new Label("resolutions", skin);
        add(resolutionsLabel);
        resolutionsSelectBox = new SelectBox<Resolution>(skin);
        add(resolutionsSelectBox);
        row();

        add(new Widget());
        fullscreenCheckbox = new CheckBox("fullscreen", skin);
        add(fullscreenCheckbox);
    }

    private void loadResolutions() {
        Resolution[] modes = getUniqueDisplayModes();
        Arrays.sort(modes, new Comparator<Resolution>() {
            @Override
            public int compare(Resolution o1, Resolution o2) {
                return o2.area() - o1.area();
            }
        });
        resolutionsSelectBox.setItems(modes);
    }

    private Resolution[] getUniqueDisplayModes() {
        Graphics.DisplayMode[] allModes = Gdx.graphics.getDisplayModes();
        Set<Resolution> resolutions = new HashSet<Resolution>();
        for (Graphics.DisplayMode mode : allModes) {
            resolutions.add(new Resolution(mode.width, mode.height));
        }
        return resolutions.toArray(new Resolution[resolutions.size()]);
    }

    private void tryToSetCurrentResolution() {
        Resolution current = getCurrentResolution();
        for (Object obj : resolutionsSelectBox.getItems()) {
            Resolution resolution = (Resolution)obj;
            if (resolution.equals(current)) {
                resolutionsSelectBox.setSelected(resolution);
                break;
            }
        }
        fullscreenCheckbox.setChecked(Gdx.graphics.isFullscreen());
    }

    private Resolution getSelectedResolution() {
        return resolutionsSelectBox.getSelected();
    }

    private Resolution getCurrentResolution() {
        return new Resolution(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void apply() {
        applyResolution();
        saveConfig();
    }

    private void applyResolution() {
        Resolution resolution = getSelectedResolution();
        game.setVideoDisplayMode(new VideoDisplayMode(resolution.width, resolution.height,
            fullscreenCheckbox.isChecked() ? ScreenMode.Fullscreen : ScreenMode.Windowed));
    }

    private void saveConfig() {
        VideoConfig config = new VideoConfig();
        config.storeDisplayMode(VideoDisplayMode.getCurrentVideoDisplayMode());
        config.flush();
    }

    @Override
    public void applyTranslation() {
        resolutionsLabel.setText(I18N.i18n.format("$resolution_colon"));
        fullscreenCheckbox.setText(I18N.i18n.format("$fullscreen"));
    }

    private class Resolution {
        public int width;
        public int height;

        public int area() {
            return width * height;
        }

        public Resolution(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Resolution that = (Resolution) o;

            if (height != that.height) return false;
            if (width != that.width) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = width;
            result = 31 * result + height;
            return result;
        }

        @Override
        public String toString() {
            return width + "x" + height;
        }
    }
}
