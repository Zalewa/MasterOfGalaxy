package masterofgalaxy.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Preferences;
import masterofgalaxy.config.VideoDisplayMode.ScreenMode;

import java.text.MessageFormat;

public class VideoConfig {
    private final Preferences prefs;

    public VideoConfig() {
        this.prefs = PrefsProvider.main();
    }

    public VideoConfig(Preferences prefs) {
        this.prefs = prefs;
    }

    public VideoDisplayMode getLastDisplayMode() {
        ScreenMode screenMode = getLastDisplayScreenMode();
        return getDisplayModeForScreenMode(screenMode);
    }

    public VideoDisplayMode getDisplayModeForScreenMode(ScreenMode screenMode) {
        final int minValidSize = 10;

        String prefix = getVideoScreenModePrefix(screenMode);
        int width = prefs.getInteger(prefix + "/Width", -1);
        int height = prefs.getInteger(prefix + "/Height", -1);

        DisplayMode desktop = Gdx.graphics.getDesktopDisplayMode();
        if (width > minValidSize && height > minValidSize && screenMode != ScreenMode.Unknown) {
            return new VideoDisplayMode(width, height, screenMode);
        } else {
            return new VideoDisplayMode(desktop.width, desktop.height, ScreenMode.Fullscreen);
        }
    }

    public ScreenMode getLastDisplayScreenMode() {
        try {
            return ScreenMode.valueOf(prefs.getString("VideoMode/ScreenMode"));
        } catch (IllegalArgumentException e) {
            return ScreenMode.Unknown;
        }
    }

    public boolean hasDisplayModeStored(ScreenMode screenMode) {
        return prefs.contains(getVideoScreenModePrefix(screenMode) + "/Width") && prefs.contains(getVideoScreenModePrefix(screenMode) + "/Height");
    }

    public void storeDisplayMode(VideoDisplayMode mode) {
        prefs.putString("VideoMode/ScreenMode", mode.screenMode.name());
        String prefix = getVideoScreenModePrefix(mode.screenMode);
        prefs.putInteger(prefix + "/Width", mode.width);
        prefs.putInteger(prefix + "/Height", mode.height);
    }

    private String getVideoScreenModePrefix(ScreenMode screenMode) {
        return MessageFormat.format("VideoMode/{0}", screenMode.name());
    }

    public void flush() {
        prefs.flush();
    }
}
