package masterofgalaxy.assets.tech;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import masterofgalaxy.gamestate.resources.ResourceDistribution;


public class TechKnowledge implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Set<String>> techs = new LinkedHashMap<String, Set<String>>();
    private List<ResearchProgress> researchProgress = new LinkedList<ResearchProgress>();
    private Map<String, String> currentResearch = new LinkedHashMap<String, String>();
    private ResourceDistribution researchDistribution = new ResourceDistribution();

    public void addTech(TechBranch branch, Tech tech) {
        getOrCreateBranch(branch.getId()).add(tech.getId());
    }

    public String getCurrentResearchOnBranch(TechBranch branch) {
        return currentResearch.get(branch.getId());
    }

    public float getCurrentResearchCostProgressOnBranch(TechBranch branch) {
        Tech tech = branch.getTech(getCurrentResearchOnBranch(branch));
        if (tech != null) {
            ResearchProgress progress = getTechProgress(branch, tech);
            if (progress != null) {
                return (float)progress.progress / (float)tech.getCost();
            }
        }
        return 0.0f;
    }

    private ResearchProgress getTechProgress(TechBranch branch, Tech tech) {
        for (ResearchProgress progress : researchProgress) {
            if (progress.isEqual(branch, tech)) {
                return progress;
            }
        }
        return null;
    }

    public float getBranchResourceDistribution(TechBranch branch) {
        return researchDistribution.getAmount(branch.getId());
    }

    public void setBranchResourceDistribution(TechBranch branch, float amount) {
        researchDistribution.setAmount(branch.getId(), amount);
    }

    public void setBranchResourceLocked(TechBranch branch, boolean locked) {
        researchDistribution.setLocked(branch.getId(), locked);
    }

    public boolean isBranchResourceLocked(TechBranch branch) {
        return researchDistribution.isLocked(branch.getId());
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

    private Set<String> getOrCreateBranch(String branchName) {
        if (techs.containsKey(branchName)) {
            return techs.get(branchName);
        } else {
            Set<String> branch = new LinkedHashSet<String>();
            techs.put(branchName, branch);
            researchDistribution.addResource(branchName);
            researchDistribution.equalize();
            return branch;
        }
    }

    public List<Tech> getTechs(TechBranch branch) {
        Set<String> ids = getOrCreateBranch(branch.getId());
        List<Tech> result = new LinkedList<Tech>();
        for (String id : ids) {
            result.add(branch.getTech(id));
        }
        return result;
    }

    public void startResearch(TechBranch branch, Tech tech) {
        createOrGetResearchProgress(branch, tech);
        currentResearch.put(branch.getId(), tech.getId());
    }

    private ResearchProgress createOrGetResearchProgress(TechBranch branch, Tech tech) {
        for (ResearchProgress candidate : researchProgress) {
            if (candidate.isEqual(branch, tech)) {
                return candidate;
            }
        }
        ResearchProgress progress = new ResearchProgress(branch, tech);
        researchProgress.add(progress);
        return progress;
    }

    public int progressResearch(TechTree techTree, int researchPoints) {
        int excessive = 0;
        float pointsPool = researchPoints;
        List<String> completedBranches = new LinkedList<String>();
        for (String branchName : currentResearch.keySet()) {
            TechBranch branch = techTree.getTechBranch(branchName);
            Tech tech = branch.getTech(currentResearch.get(branchName));
            ResearchProgress progress = getTechProgress(branch, tech);
            float distributedPoints = researchPoints * getBranchResourceDistribution(branch);
            progress.progress += distributedPoints;
            pointsPool -= distributedPoints;
            if (progress.progress >= tech.getCost()) {
                completeResearch(branch, tech);
                completedBranches.add(branchName);
                excessive += tech.getCost() - progress.progress;
            }
        }
        excessive += (int)pointsPool;
        for (String branchName : completedBranches) {
            currentResearch.remove(branchName);
        }
        return excessive;
    }

    private void completeResearch(TechBranch branch, Tech tech) {
        addTech(branch, tech);
        removeTechProgress(branch, tech);
    }

    private void removeTechProgress(TechBranch branch, Tech tech) {
        researchProgress.remove(getTechProgress(branch, tech));
    }
}
