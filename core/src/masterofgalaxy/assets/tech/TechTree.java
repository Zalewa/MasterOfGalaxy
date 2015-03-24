package masterofgalaxy.assets.tech;

import java.util.LinkedHashMap;
import java.util.Map;

public class TechTree {
    private Map<String, TechBranch> branches = new LinkedHashMap<String, TechBranch>();

    public TechTree() {
    }

    public void appendBranch(TechBranch branch) {
        branches.put(branch.getId(), branch);
    }
}
