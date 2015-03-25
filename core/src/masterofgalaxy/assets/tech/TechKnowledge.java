package masterofgalaxy.assets.tech;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import masterofgalaxy.Strings;

public class TechKnowledge implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Set<String>> techs = new LinkedHashMap<String, Set<String>>();

    public void addTech(TechBranch branch, Tech tech) {
        getOrCreateBranch(branch.getId()).add(tech.getId());
    }

    public boolean hasTech(String techPath) {
        String[] pathTokens = techPath.split("/");
        return hasTech(pathTokens[0], pathTokens[1]);
    }

    public boolean hasTech(TechBranch branch, Tech tech) {
        return hasTech(branch.getId(), tech.getId());
    }

    public boolean hasTech(TechBranch branch, String tech) {
        return hasTech(branch.getId(), tech);
    }

    public boolean hasTech(String branch, String tech) {
        if (techs.containsKey(branch)) {
            return techs.get(branch).contains(tech);
        }
        return false;
    }

    public boolean isResearchable(TechBranch branch, Tech tech) {
        if (hasTech(branch, tech)) {
            return false;
        }
        return Strings.isNullOrEmpty(tech.getParentId()) || hasTech(branch, tech.getParentId());
    }

    private Set<String> getOrCreateBranch(String branchName) {
        if (techs.containsKey(branchName)) {
            return techs.get(branchName);
        } else {
            Set<String> branch = new LinkedHashSet<String>();
            techs.put(branchName, branch);
            return branch;
        }
    }
}
