package edu.ncsu.csc216.tracker.model;


import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.tracker.requirement.Command;
import edu.ncsu.csc216.tracker.requirement.Requirement;
import edu.ncsu.csc216.tracker.xml.Req;
import edu.ncsu.csc216.tracker.xml.RequirementIOException;
import edu.ncsu.csc216.tracker.xml.RequirementsReader;
import edu.ncsu.csc216.tracker.xml.RequirementsWriter;

/**
 * RequirementsTrackerModel class controls the creation and modification of (potentially many) RequirementsLists and handles CommandValues from the GUI.
 * Implements the Singleton design pattern to ensure that all parts of the RequirementsTrackerGUI are interacting with the same RequirementsTrackerModel at all times.
 * RequirementsTrackerModel maintains the current RequirementsList and handles activity around loading, saving, and creating new RequirementsLists. For this, RequirementsTrackerModel will use RequirementReader and RequirementWriter from the RequirementTrackerXML library. 
 * If a RequirementIOException is thrown, RequirementsTrackerModel catches it and throws a new IllegalArgumentException.
 * @author mlee25 Michael Lee
 *
 */
public class RequirementsTrackerModel {

	/** Instance variable for RequirementsTrackerModel */
	private static RequirementsTrackerModel instance;
	
	/** Instance variable for RequirementsList */
	private RequirementsList reqList;
	
	/**
	 * Constructor for RequirementsTrackerModel.  Constructor is private.
	 */
	private RequirementsTrackerModel() {
		instance = this;
		this.createNewRequirementsList();
	}
	
	/**
	 * Getter for RequirementsTrackerModel.  
	 * @return the single instance of RequirementsTrackerModel
	 */
	public static RequirementsTrackerModel getInstance() {
		if (instance != null) {
			return instance;			
		}
		instance = new RequirementsTrackerModel();
		return instance;
	}
	
	/**
	 * Saves a RequirementsList to the specified filename using RequirementWriter from the RequirementTrackerXML library.
	 * @param filename file to write
	 * @throws IllegalArgumentException if a RequirementIOException is thrown by RequirementWriter
	 */
	public void saveRequirementsToFile(String filename) throws IllegalArgumentException {
/*
		RequirementsWriter writer = new RequirementsWriter("test_files/" + filename);
		ArrayList<Requirement> list = reqList.getRequirements();		

		for (int i = 0; i < list.size(); i++) {
			writer.addReq(list.get(i).getXMLReq());
		}
		try {
			writer.marshal();			
		} catch (RequirementIOException e) {
			throw new IllegalArgumentException("write exception");
		}
*/
	}
	
	/**
	 * Loads a RequirementsList from the specified filename using RequirementReader from the RequirementTrackerXML library.
	 * @param filename file to read
	 * @throws IllegalArgumentException if a RequirementIOException is thrown by RequirementReader
	 */
	public void loadRequirementsFromFile(String filename) throws IllegalArgumentException {
/*		RequirementsReader reader = null;
		instance.createNewRequirementsList();
		List<Req> list = new ArrayList<Req>();
		try {
			reader = new RequirementsReader(filename);
			list = reader.getReqs();
		} catch (RequirementIOException e) {
			throw new IllegalArgumentException("my exception message for load");
		}
		reqList.addXMLReqs(list);
*/
	}
	
	/**
	 * Creates a new RequirementsList.
	 */
	public void createNewRequirementsList() {
		reqList = new RequirementsList();
	}
	
	/**
	 * Gets a 2D Object array that is used to populate the RequirementTableModel (inner class of the RequirementsTrackerGUI). 
	 * The 2D Object array stores [rows][columns] with 1 row for every Requirement and 3 columns for each Requirement’s id number, state name and summary.
	 * @return the 2D Object array
	 */
	public Object[][] getRequirementListAsArray() {
		Requirement r = null;
		ArrayList<Requirement> list = reqList.getRequirements();		
		Object[][] result = new Object[list.size()][3];

		for (int i = 0; i < list.size(); i++) {
			r = list.get(i);
			result[i][0] = r.getRequirementId();
			result[i][1] = r.getState();
			result[i][2] = r.getSummary();			
		}
		return result;
	}
	
	/**
	 * Gets a Requirement with the specified id number.
	 * @param reqId the id number of the Requirement
	 * @return the Requirement with the specified id number
	 */
	public Requirement getRequirementById(int reqId) {
		return reqList.getRequirementById(reqId);
	}
	
	/**
	 * Executes the specified Command on the Requirement with the specified id number.
	 * @param reqId the id number of the Requirement to issue the command to
	 * @param c the Command to be issued
	 * @throws UnsupportedOperationException if the Command is invalid for the state
	 */
	public void executeCommand(int reqId, Command c) throws UnsupportedOperationException {
		try {
			reqList.executeCommand(reqId, c);
		} catch (UnsupportedOperationException e) {
			throw new UnsupportedOperationException("RequirementsTrackerModel exception thrown");
		}
	}
	
	/**
	 * Deletes a Requirement with the specified id number.
	 * @param reqId the id number of the Requirement
	 */
	public void deleteRequirementById(int reqId) {
		reqList.deleteRequirementById(reqId);
	}
	
	/**
	 * Adds a new Requirement with the specified summary and acceptance test id.
	 * @param summary the Requirement's summary
	 * @param acceptanceTestId the Requirement's acceptance test id
	 */
	public void addRequirement(String summary, String acceptanceTestId) {
		reqList.addRequirement(summary, acceptanceTestId);
	}
	
}
