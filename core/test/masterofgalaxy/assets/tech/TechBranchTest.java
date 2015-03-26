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
        knowledge.addTech(branch, getTechWithDescendant());
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
        Tech tech1 = new Tech();
        tech1.setId("1");

        Tech tech2 = getTechWithDescendant();

        Tech tech3 = new Tech();
        tech3.setId("3");
        tech3.setParentId("2");

        TechBranch branch = new TechBranch();
        branch.appendTech(tech1);
        branch.appendTech(tech2);
        branch.appendTech(tech3);
        return branch;
    }

    private Tech getTechWithDescendant() {
        Tech tech2 = new Tech();
        tech2.setId("2");
        return tech2;
    }

}
