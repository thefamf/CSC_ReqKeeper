/**
 * 
 */
package edu.ncsu.csc216.tracker.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.tracker.requirement.Command;
import edu.ncsu.csc216.tracker.requirement.Requirement;
import edu.ncsu.csc216.tracker.requirement.enums.CommandValue;
import edu.ncsu.csc216.tracker.requirement.enums.Rejection;
import edu.ncsu.csc216.tracker.xml.Req;

/**
 * Test class for RequirementsList class
 * @author mlee25 Michael Lee
 */
public class RequirementsListTest {

	/** Valid req list */
	private final String validTestFile = "test-files/req1.xml";
	
	/**
	 * Setup for testing RequirementsList class
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for RequirementsList constructor
	 */
	@Test
	public void testRequirementsList() {
		RequirementsList reqlist = new RequirementsList();
		assertEquals(0, reqlist.getRequirements().size());
	}

	/**
	 * Test method for addRequirement
	 */
	@Test
	public void testAddRequirement() {
		RequirementsList reqlist = new RequirementsList();
		//Test add
		int id = reqlist.addRequirement("summary", "acceptanceTestId");
		assertEquals(1, reqlist.getRequirements().size());
		assertEquals(0, id);
		//Test add 2
		id = reqlist.addRequirement("summary2", "acceptanceTestId2");
		assertEquals(2, reqlist.getRequirements().size());
		assertEquals(1, id);
	}

	/**
	 * Test method for addXMLReqs
	 */
	@Test
	public void testAddXMLReqs() {
		RequirementsList reqlist = new RequirementsList();
		ArrayList<Req> xmlReqs = new ArrayList<Req>();
		
		int id1 = reqlist.addRequirement("summary", "acceptanceTestId");
		assertEquals(1, reqlist.getRequirements().size());
		assertEquals(0, id1);
		int id2 = reqlist.addRequirement("summary2", "acceptanceTestId2");
		assertEquals(2, reqlist.getRequirements().size());
		assertEquals(1, id2);

		//Create list of XML reqs
		Req x1 = reqlist.getRequirementById(id1).getXMLReq();
		Req x2 = reqlist.getRequirementById(id2).getXMLReq();
		xmlReqs.add(x1);
		xmlReqs.add(x2);
		//Test add
		reqlist = new RequirementsList();
		reqlist.addXMLReqs(xmlReqs);
		assertEquals(2, reqlist.getRequirements().size());
	}

	/**
	 * Test method for getRequirements
	 */
	@Test
	public void testGetRequirements() {
		RequirementsList reqlist = new RequirementsList();
		int id1 = reqlist.addRequirement("summary", "acceptanceTestId");
		assertEquals(1, reqlist.getRequirements().size());
		assertEquals(0, id1);
		int id2 = reqlist.addRequirement("summary2", "acceptanceTestId2");
		assertEquals(2, reqlist.getRequirements().size());
		assertEquals(1, id2);
	}

	/**
	 * Test method for getRequirementById
	 */
	@Test
	public void testGetRequirementById() {
		RequirementsList reqlist = new RequirementsList();
		int id1 = reqlist.addRequirement("summary", "acceptanceTestId");
		assertEquals(1, reqlist.getRequirements().size());
		assertEquals(0, id1);

		Requirement r = null;
		r = reqlist.getRequirementById(id1);
		assertEquals("summary", r.getSummary());
		assertEquals("acceptanceTestId", r.getAcceptanceTestId());
	}

	/**
	 * Test method for executeCommand
	 */
	@Test
	public void testExecuteCommand() {
		RequirementsList reqlist = new RequirementsList();
		int id1 = reqlist.addRequirement("summary", "acceptanceTestId");
		assertEquals(1, reqlist.getRequirements().size());
		assertEquals(0, id1);
		Command c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		reqlist.executeCommand(id1, c);
		Requirement r = reqlist.getRequirementById(id1);
		assertEquals("Accepted", r.getState().getStateName());
		assertEquals("estimate", r.getEstimate());
		assertEquals(1, r.getPriority());
	}

	/**
	 * Test method for deleteRequirementById
	 */
	@Test
	public void testDeleteRequirementById() {
		RequirementsList reqlist = new RequirementsList();
		//Add two requirements
		int id1 = reqlist.addRequirement("summary", "acceptanceTestId");
		assertEquals(1, reqlist.getRequirements().size());
		assertEquals(0, id1);
		int id2 = reqlist.addRequirement("summary2", "acceptanceTestId2");
		assertEquals(2, reqlist.getRequirements().size());
		assertEquals(1, id2);
		//Delete first requirement
		reqlist.deleteRequirementById(id1);
		assertEquals(1, reqlist.getRequirements().size());
		Requirement r = reqlist.getRequirementById(id2);
		assertEquals("summary2", r.getSummary());
		assertEquals("acceptanceTestId2", r.getAcceptanceTestId());		
	}

}
