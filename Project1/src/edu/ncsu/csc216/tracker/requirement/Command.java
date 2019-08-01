package edu.ncsu.csc216.tracker.requirement;

import edu.ncsu.csc216.tracker.requirement.enums.CommandValue;
import edu.ncsu.csc216.tracker.requirement.enums.Rejection;

/**
 * Command class that represents a transition command to be issued to a Requirement.
 * 
 * @author mlee25 Michael Lee
 *
 */
public class Command {

	/** instance variable for CommandValue */
	private CommandValue command;
	
	/** The summary to include with the Command, if applicable */
	private String summary;

	/** The acceptanceTestId to include with the Command, if applicable */
	private String acceptanceTestId; 

	/** The priority to include with the Command, if applicable */
	private int priority;
	
	/** The estimate to include with the Command, if applicable */
	private String estimate;
	
	/** The developerId to include with the Command, if applicable */
	private String developerId;
	
	/** instance variable for Rejection */
	private Rejection reason;
	
	/**
	 * Constructor for Command class. A Command encapsulates the seven possible user actions. 
	 * A Command is created by a message from the GUI and is issued down through RequirementTrackerModel, RequirementsList, Requirement, and RequirementState.
	 * This constructor handles precondition checks for the parameters associated with each command.
	 * 
	 * @param c the CommandValue enumeration for this Command
	 * @param summary the summary for this Command
	 * @param acceptanceTestId the acceptanceTestId for this Command
	 * @param priority the priority for this Command
	 * @param estimate the estimate for this Command
	 * @param developerId the developerId for this Command
	 * @param reason the Rejection enumeration for this Command
	 * @throws IllegalArgumentException if a required parameter for the Command is invalid
	 */
	public Command(CommandValue c, String summary, String acceptanceTestId, int priority, String estimate, String developerId, Rejection reason) throws IllegalArgumentException {
		if (c == null) {
			throw new IllegalArgumentException("Invalid command");
		}
		if (c == CommandValue.ACCEPT) {
			//TODO throw new IllegalArgumentException("Invalid priority");
			if (priority < 1 || priority > 3 || estimate == null || estimate.equals("")) {
				throw new IllegalArgumentException("Invalid command");				
			}
		}		
		if (c == CommandValue.ASSIGN && (developerId == null || developerId.equals(""))) {
			throw new IllegalArgumentException("Invalid command");
		}
		// if (c == CommandValue.COMPLETE) {}
		// no check required
		// if (c == CommandValue.PASS) {}
		// no check required
		// if (c == CommandValue.FAIL) {}
		// no check required
		if (c == CommandValue.REVISE && (summary == null || summary.equals("") || acceptanceTestId == null || acceptanceTestId.equals(""))) {
			throw new IllegalArgumentException("Invalid command");
		}
		if (c == CommandValue.REJECT && reason == null) {
			throw new IllegalArgumentException("Invalid command");
		}
		this.command = c;
		this.summary = summary;
		this.acceptanceTestId = acceptanceTestId;
		this.priority = priority;
		this.estimate = estimate;
		this.developerId = developerId;
		this.reason = reason;

	}
	
	/**
	 * Getter for this Command's summary.
	 * @return the summary
	 */
	public String getSummary() {
		return this.summary;
	}
	
	/**
	 * Getter for this Command's acceptanceTestId.
	 * @return the acceptanceTestId
	 */
	public String getAcceptanceTestId() {
		return acceptanceTestId;
	}
	
	/**
	 * Getter for this Command's CommandValue.
	 * @return the CommandValue
	 */
	public CommandValue getCommand() {
		return command;
	}
	
	/**
	 * Getter for this Command's estimate.
	 * @return the estimate
	 */
	public String getEstimate() {
		return this.estimate;
	}
	
	/**
	 * Getter for this Command's CommandValue.
	 * @return the priority
	 */
	public int getPriority() {
		return this.priority;
	}
	
	/**
	 * Getter for this Command's developerId.
	 * @return the developerId
	 */
	public String getDeveloperId() {
		return this.developerId;		
	}
	
	/**
	 * Getter for this Command's Rejection reason.
	 * @return the Rejection reason
	 */
	public Rejection getRejectionReason() {
		return reason;
	}
}

