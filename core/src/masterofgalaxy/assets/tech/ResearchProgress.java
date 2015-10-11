package masterofgalaxy.assets.tech;

public class ResearchProgress {
    public String branch;
    public String tech;
    public float progress;

    public ResearchProgress() {}

    public ResearchProgress(TechBranch branch, Tech tech) {
        this.branch = branch.getId();
        this.tech = tech.getId();
    }

    public boolean isEqual(TechBranch branch, Tech tech) {
        return this.branch.equals(branch.getId()) && this.tech.equals(tech.getId());
    }
}
