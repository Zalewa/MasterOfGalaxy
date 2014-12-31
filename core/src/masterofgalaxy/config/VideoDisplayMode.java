package masterofgalaxy.config;

import com.badlogic.gdx.Gdx;

import java.text.MessageFormat;

public class VideoDisplayMode {
    public enum ScreenMode {
        Fullscreen, Windowed, Unknown
    }

    public static VideoDisplayMode getCurrentVideoDisplayMode() {
        return new VideoDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), getCurrentScreenMode());
    }

    public static ScreenMode getCurrentScreenMode() {
        return Gdx.graphics.isFullscreen() ? ScreenMode.Fullscreen : ScreenMode.Windowed;
    }

    public final int width;
    public final int height;
    public final ScreenMode screenMode;

    public VideoDisplayMode(int width, int height, ScreenMode screenMode) {
        this.screenMode = screenMode;
        this.height = height;
        this.width = width;
    }

    public boolean isFullscreen() {
        return screenMode == ScreenMode.Fullscreen;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}: {1}x{2}x{3}", getClass().getSimpleName(), width, height, screenMode);
    }
}
