package masterofgalaxy.assets.tech;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class TechTree {
    private Map<String, TechBranch> branches = new LinkedHashMap<String, TechBranch>();

    public TechTree() {
    }

    public void appendBranch(TechBranch branch) {
        branches.put(branch.getId(), branch);
    }

    public Collection<TechBranch> getBranches() {
        return branches.values();
    }

    public TechBranch getTechBranch(String name) {
        return branches.get(name);
    }
}
