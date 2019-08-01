package edu.ncsu.csc216.tracker.model;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.tracker.requirement.Command;
import edu.ncsu.csc216.tracker.requirement.Requirement;
import edu.ncsu.csc216.tracker.xml.Req;

/**
 * RequirementsList class maintains a ArrayList of Requirements.  
 * Can add and remove a Requirement from the List, search for a Requirement in the List, update a Requirement in the List through execution of a Command, return the entire List or sublists of itself.
 * @author mlee25 Michael Lee
 */
public class RequirementsList {
	// When working with methods that receive a requirementId parameter, there is no need to error check or throw an exception if the requirementId does not exist in the list. For getRequirementById, return null. 
	// For all other methods, do not change the internal state of the list.

	/** Instance variable for List of Requirements */
	private ArrayList<Requirement> reqs;
	
	/**
	 * Constructor for RequirementsList.  
	 */
	public RequirementsList() {
	// When creating a new RequirementsList, reset the Requirement’s counter to 0. 
			reqs = new ArrayList<Requirement>();
			Requirement.setCounter(0);
	}
	
	/**
	 * Adds a Requirement to the list and handles the counter.  
	 * @param summary the Requirement's summary
	 * @param acceptanceTestId the Requirement's acceptanceTestId
	 * @return the requirementId assigned to this Requirement
	 */
	public int addRequirement(String summary, String acceptanceTestId) {
		Requirement r = new Requirement(summary, acceptanceTestId);
		reqs.add(r);
		return r.getRequirementId();
	}
	
	/**
	 * Adds Requirements from a List of XML Req objects.
	 * @param xmlReqs the List of XML Req objects
	 */
	public void addXMLReqs(List<Req> xmlReqs) {
		Requirement requirement = null;
		int maxId = 0;
		for (int i = 0; i < xmlReqs.size(); i++) {
			requirement = new Requirement(xmlReqs.get(i));
			reqs.add(requirement);
			if (requirement.getRequirementId() > maxId) {
				maxId = requirement.getRequirementId();
			}
		}
		Requirement.setCounter(maxId);
	}

	/**
	 * Getter for an ArrayList of Requirements.
	 * @return the ArrayList of Requirements
	 */
	public ArrayList<Requirement> getRequirements() {
		return reqs;
	}
	
	/**
	 * Getter for a Requirement based on its unique requirementId.
	 * @param requirementId the unique requirementId
	 * @return the Requirement
	 */
	public Requirement getRequirementById(int requirementId) {
		// When working with methods that receive a requirementId parameter, there is no need to error check or throw an exception if the requirementId does not exist in the list. For getRequirementById, return null. 
		// For all other methods, do not change the internal state of the list.
		// Requirement r = null;
		for (int i = 0; i < reqs.size(); i++) {
			if (reqs.get(i).getRequirementId() == requirementId) {
				return reqs.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Executes a Command for the Requirement with the specified requirementId.
	 * @param requirementId the unique requirementId
	 * @param c the Command to execute
	 * @throws UnsupportedOperationException if the Command is invalid for the state of the Requirement
	 */
	public void executeCommand(int requirementId, Command c) throws UnsupportedOperationException {
		try {
			for (int i = 0; i < reqs.size(); i++) {
				if (reqs.get(i).getRequirementId() == requirementId) {
					reqs.get(i).update(c);
				}
			}
		} catch (UnsupportedOperationException e) {
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * Deletes the Requirement with the specified requirementId.
	 * Does not error check or throw an exception if the requirementId does not exist in the List.
	 * @param requirementId the unique requirementId
	 */
	public void deleteRequirementById(int requirementId) {
		for (int i = 0; i < reqs.size(); i++) {
			if (reqs.get(i).getRequirementId() == requirementId) {
				reqs.remove(i);
			}
		}
	}

}
