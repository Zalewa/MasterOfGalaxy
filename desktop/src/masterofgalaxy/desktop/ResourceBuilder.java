package masterofgalaxy.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class ResourceBuilder {
    public static boolean isResourceBuildEnabled(String[] args) {
        for (String arg : args) {
            if (arg.equals("--build-resources")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Working directory must be set to "android/assets".
     */
    public void build() {
        TexturePacker.processIfModified("ui/skin", "ui", "uiskin");
    }

    public static void main(String[] args) {
        new ResourceBuilder().build();
    }
}
