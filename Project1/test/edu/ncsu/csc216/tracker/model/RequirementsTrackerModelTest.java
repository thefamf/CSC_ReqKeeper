/**
 * 
 */
package edu.ncsu.csc216.tracker.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.tracker.requirement.Command;
import edu.ncsu.csc216.tracker.requirement.Requirement;
import edu.ncsu.csc216.tracker.requirement.RequirementState;
import edu.ncsu.csc216.tracker.requirement.enums.CommandValue;
import edu.ncsu.csc216.tracker.requirement.enums.Rejection;

/**
 * Test class for RequirementsTrackerModel class
 * @author mlee25 Michael Lee
 */
public class RequirementsTrackerModelTest {

	/** Valid req list */
	private final String validTestFile = "test-files/req1.xml";
	

	/**
	 * Setup for testing RequirementsTrackerModel
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
/*		Path sourcePath = FileSystems.getDefault().getPath("test-files", "expected_requirements.txt");
		Path destinationPath = FileSystems.getDefault().getPath("test-files", "actual_requirements.txt");
		try {
			Files.deleteIfExists(destinationPath);
			Files.copy(sourcePath, destinationPath);
		} catch (IOException e) {
			fail("Unable to reset files");
		}
*/		
	}

	/**
	 * Test method for getInstance
	 */
	@Test
	public void testGetInstance() {
		RequirementsTrackerModel model = RequirementsTrackerModel.getInstance();
		assertNotNull(model);
	}

	/**
	 * Test method for loadRequirementsFromFile
	 */
	@Test
	public void testLoadRequirementsFromFile() {
		RequirementsTrackerModel model = RequirementsTrackerModel.getInstance();
		assertNotNull(model);
/*		model.loadRequirementsFromFile("test_files/req1.xml");
		Object[][] array = model.getRequirementListAsArray();
		assertEquals(5, array.length);
*/
	}

	/**
	 * Test method for saveRequirementsToFile
	 */
	@Test
	public void testSaveRequirementsToFile() {
		RequirementsTrackerModel model = RequirementsTrackerModel.getInstance();
		assertNotNull(model);
	}

	/**
	 * Test method for createNewRequirementsList
	 */
	@Test
	public void testCreateNewRequirementsList() {
		RequirementsTrackerModel model = RequirementsTrackerModel.getInstance();
		model.createNewRequirementsList();
		Object[][] array = model.getRequirementListAsArray();
		assertEquals(0, array.length);
	}

	/**
	 * Test method for addRequirement
	 */
	@Test
	public void testAddRequirement() {
		RequirementsTrackerModel model = RequirementsTrackerModel.getInstance();
		model.createNewRequirementsList();
		model.addRequirement("summary", "acceptanceTestId");
		Object[][] array = model.getRequirementListAsArray();
		//RequirementState state = (RequirementState) array[0][1];
		assertEquals(1, array.length);
	}


	/**
	 * Test method for getRequirementListAsArray
	 */
	@Test
	public void testGetRequirementListAsArray() {
		RequirementsTrackerModel model = RequirementsTrackerModel.getInstance();
		model.createNewRequirementsList();
		model.addRequirement("summary", "acceptanceTestId");
		Object[][] array = model.getRequirementListAsArray();
		RequirementState state = (RequirementState) array[0][1];
		assertEquals(1, array.length);
		assertEquals(0, array[0][0]);
		assertEquals("Submitted", state.getStateName());
		assertEquals("summary", array[0][2]);
		
	}

	/**
	 * Test method for getRequirementById
	 */
	@Test
	public void testGetRequirementById() {
		RequirementsTrackerModel model = RequirementsTrackerModel.getInstance();
		model.createNewRequirementsList();
		model.addRequirement("summary", "acceptanceTestId");

		Requirement r = model.getRequirementById(0);
		assertEquals("Submitted", r.getState().getStateName());
		assertEquals("summary", r.getSummary());		
	}

	/**
	 * Test method for executeCommand
	 */
	@Test
	public void testExecuteCommand() {
		RequirementsTrackerModel model = RequirementsTrackerModel.getInstance();
		model.createNewRequirementsList();
		model.addRequirement("summary", "acceptanceTestId");
		Command c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		model.executeCommand(0, c);
		Requirement r = model.getRequirementById(0);
		assertEquals("Accepted", r.getState().getStateName());
		assertEquals("summary", r.getSummary());		
		assertEquals("estimate", r.getEstimate());
		assertEquals(1, r.getPriority());
	}

	/**
	 * Test method for deleteRequirementById
	 */
	@Test
	public void testDeleteRequirementById() {
		RequirementsTrackerModel model = RequirementsTrackerModel.getInstance();
		model.createNewRequirementsList();
		model.addRequirement("summary", "acceptanceTestId");
		Object[][] array = model.getRequirementListAsArray();
		assertEquals(1, array.length);
		model.deleteRequirementById(0);
	}

}
