package masterofgalaxy.mainmenu.options;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import masterofgalaxy.assets.I18N;
import masterofgalaxy.config.VideoConfig;
import masterofgalaxy.config.VideoDisplayMode;

import java.util.*;

public class VideoModeOptions extends Table {
    private CheckBox fullscreenCheckbox;
    private Label resolutionsLabel;
    private SelectBox resolutionsSelectBox;

    public VideoModeOptions(Skin skin) {
        super(skin);
        I18N.localeChanged.add(new Listener<Object>() {
            @Override
            public void receive(Signal<Object> signal, Object object) {
                applyTranslation();
            }
        });

        setupUi(skin);
        loadResolutions();
        tryToSetCurrentResolution();

        applyTranslation();
    }

    private void setupUi(Skin skin) {
        defaults().left().space(10.0f);

        resolutionsLabel = new Label("resolutions", skin);
        add(resolutionsLabel);
        resolutionsSelectBox = new SelectBox(skin);
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
        resolutionsSelectBox.setItems((Object[])modes);
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
        return (Resolution) resolutionsSelectBox.getSelected();
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
        Gdx.graphics.setDisplayMode(resolution.width, resolution.height, fullscreenCheckbox.isChecked());
    }

    private void saveConfig() {
        VideoConfig config = new VideoConfig();
        config.storeDisplayMode(VideoDisplayMode.getCurrentVideoDisplayMode());
        config.flush();
    }

    private void applyTranslation() {
        resolutionsLabel.setText(I18N.i18n.format("$resolution"));
        fullscreenCheckbox.setText(I18N.i18n.format("$fullscreen"));
    }

    private class Resolution {
        public int width;
        public int height;

        public int area() {
            return width * height;
        }

        public Resolution() {
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
