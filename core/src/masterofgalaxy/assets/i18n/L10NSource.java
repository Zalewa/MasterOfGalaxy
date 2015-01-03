package masterofgalaxy.assets.i18n;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

public class L10NSource {
    private I18NBundle bundle;
    private FileHandle source;

    public L10NSource(I18NBundle bundle, FileHandle source) {
        this.bundle = bundle;
        this.source = source;
    }

    public I18NBundle getBundle() {
        return bundle;
    }

    public FileHandle getSource() {
        return source;
    }
}
