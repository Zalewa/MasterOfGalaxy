package masterofgalaxy.assets.tech;

import java.util.List;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TechBranchTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testResearchableNoKnowledge() {
        TechBranch branch = mkTechBranch();

        TechKnowledge knowledge = new TechKnowledge();
        List<Tech> techs = branch.getResearchableTechs(knowledge);

        assertEquals(2, techs.size());
        assertEquals("1", techs.get(0).getId());
        assertEquals("2", techs.get(1).getId());
    }

    @Test
    public void testResearchableKnowledgeOfNode() {
        TechBranch branch = mkTechBranch();

        TechKnowledge knowledge = new TechKnowledge();
        knowledge.addTech(branch, getTech2());
        List<Tech> techs = branch.getResearchableTechs(knowledge);

        assertEquals(2, techs.size());
        assertEquals("1", techs.get(0).getId());
        assertEquals("3", techs.get(1).getId());
    }

    @Test
    public void testResearchableKnowledgeOfAll() {
        TechBranch branch = mkTechBranch();

        TechKnowledge knowledge = new TechKnowledge();
        for (Tech tech : branch.getTechs()) {
            knowledge.addTech(branch, tech);
        }
        List<Tech> techs = branch.getResearchableTechs(knowledge);

        assertEquals(0, techs.size());
    }

    private TechBranch mkTechBranch() {
        Tech tech1 = getTech1();
        Tech tech2 = getTech2();
        Tech tech3 = getTech3();

        TechTier tier1 = new TechTier();
        tier1.appendTech(tech1);
        tier1.appendTech(tech2);

        TechTier tier2 = new TechTier();
        tier2.appendTech(tech3);

        TechBranch branch = new TechBranch();
        branch.appendTechTier(tier1);
        branch.appendTechTier(tier2);
        return branch;
    }

    private Tech getTech3() {
        Tech tech3 = new Tech();
        tech3.setId("3");
        return tech3;
    }

    private Tech getTech2() {
        Tech tech2 = new Tech();
        tech2.setId("2");
        return tech2;
    }

    private Tech getTech1() {
        Tech tech1 = new Tech();
        tech1.setId("1");
        return tech1;
    }
}
