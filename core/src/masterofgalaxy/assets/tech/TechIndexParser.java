package masterofgalaxy.assets.tech;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class TechIndexParser {
    public static TechTree load(FileHandle file) {
        String[] names = readTechBundlesNames(file);
        return loadTree(names);
    }

    private static String[] readTechBundlesNames(FileHandle file) {
        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(file).get(0);

        String[] names = new String[root.size];
        for (int i = 0; i < names.length; ++i) {
            names[i] = root.getString(i);
        }
        return names;
    }

    private static TechTree loadTree(String[] bundleNames) {
        TechTree tree = new TechTree();
        for (String bundle : bundleNames) {
            tree.appendBranch(loadBranch(bundle));
        }
        return tree;
    }

    private static TechBranch loadBranch(String bundleName) {
        FileHandle file = Gdx.files.internal(bundleName);
        TechBranchParser parser = new TechBranchParser();
        return parser.load(file);
    }
}
