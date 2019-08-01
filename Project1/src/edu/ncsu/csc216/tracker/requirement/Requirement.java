package edu.ncsu.csc216.tracker.requirement;

import edu.ncsu.csc216.tracker.requirement.enums.CommandValue;
import edu.ncsu.csc216.tracker.requirement.enums.Rejection;
import edu.ncsu.csc216.tracker.xml.Req;

/**
 * Requirement class that represents a requirement tracked by the system. 
 * Requirement is the Context class of the State design pattern. 
 * It maintains the Requirement’s current state and delegates Commands for the current state to handle.
 * A Requirement knows its requirementID, state, summary, estimate, developer, acceptanceTestId, and its Rejection reason. 
 * Includes inner classes for the six concrete state classes, which implement RequirementState.
 * 
 * @author mlee25 Michael Lee
 *
 */
public class Requirement {

	/** Constant for state name: Submitted */
	public static final String SUBMITTED_NAME = "Submitted";
	/** Constant for state name: Accepted */
	public static final String ACCEPTED_NAME = "Accepted";
	/** Constant for state name: Rejected */
	public static final String REJECTED_NAME = "Rejected";
	/** Constant for state name: Working */
	public static final String WORKING_NAME = "Working";
	/** Constant for state name: Completed */
	public static final String COMPLETED_NAME = "Completed";
	/** Constant for state name: Verified */
	public static final String VERIFIED_NAME = "Verified";
	/** Constant for rejection reason: Duplicate */
	public static final String DUPLICATE_NAME = "Duplicate";
	/** Constant for rejection reason: Infeasible */
	public static final String INFEASIBLE_NAME = "Infeasible";
	/** Constant for rejection reason: Too Large */
	public static final String TOO_LARGE_NAME = "Too large";
	/** Constant for rejection reason: Out of Scope */
	public static final String OUT_OF_SCOPE_NAME = "Out of Scope";
	/** Constant for rejection reason: Inappropriate */
	public static final String INAPPROPRIATE_NAME = "Inappropriate";

	/** Requirement counter variable */
	private static int counter = 0;
	
	/** Requirement's id number */
	private int requirementId;
	
	/** Requirement's summary */
	private String summary;
	
	/** Requirement's acceptance test id */
	private String acceptanceTestId;
	
	/** Requirement's priority */
	private int priority;
	
	/** Requirement's estimate */
	private String estimate;
	
	/** Requirement's developer */
	private String developer;
	
	/** instance variable for Rejection */
	private Rejection reason;
	
	/** instance variable for AcceptedState */
	private AcceptedState accepted = new AcceptedState();
	
	/** instance variable for CompletedState */
	private CompletedState completed = new CompletedState();
	
	/** instance variable for RejectedState */
	private RejectedState rejected = new RejectedState();
	
	/** instance variable for SubmittedState */
	private SubmittedState submitted = new SubmittedState();
	
	/** instance variable for VerifiedState */
	private VerifiedState verified = new VerifiedState();
	
	/** instance variable for WorkingState */
	private WorkingState working = new WorkingState();
	
	/** instance variable for current state */
	private RequirementState state = new SubmittedState();
	
	/**
	 * Constructor for Requirement. Sets the state to Submitted and sets the summary and the acceptance test id.
	 * @param summary the Requirement's summary
	 * @param acceptanceTestId the Requirement's acceptance test id
	 */
	public Requirement(String summary, String acceptanceTestId) {
		if(summary == null || acceptanceTestId == null) {
			throw new IllegalArgumentException("null summary or id");
		} 
		this.summary = summary;			
		setAcceptanceTestId(acceptanceTestId);
		this.setState("Submitted");
		this.requirementId = counter;
		incrementCounter();
	}
	
	/**
	 * Constructor for Requirement based on a Req object from the RequirementTrackerXML library.
	 * 
	 * @param r the Req object used to create a Requirement
	 */
	public Requirement(Req r) {
		this(r.getSummary(), r.getTest());
	}
	
	/**
	 * Increments the counter.
	 */
	private static void incrementCounter() {
		counter++;
	}
	
	/**
	 * Getter for this Requirement's requirementId.
	 * @return the requirementId
	 */
	public int getRequirementId() {
		return this.requirementId;
	}
	
	/**
	 * Getter for the RequirementState object representing this Requirement's current state.
	 * @return this Requirement's RequirementState object
	 */
	public RequirementState getState() {
		return this.state;
	}
	
	/**
	 * Setter for this Requirement's state.
	 * @param state the state to be set
	 */
	private void setState(String s) {

		if (s.equals(ACCEPTED_NAME)) {
			this.state = accepted;
		}
		if (s.equals(COMPLETED_NAME)) {
			this.state = completed;
		}
		if (s.equals(REJECTED_NAME)) {
			this.state = rejected;
		}
		if (s.equals(SUBMITTED_NAME)) {
			this.state = submitted;
		}
		if (s.equals(VERIFIED_NAME)) {
			this.state = verified;
		}
		if (s.equals(WORKING_NAME)) {
			this.state = working;
		}
	}
	
	/**
	 * Getter for this Requirement's priority.
	 * @return the priority
	 */
	public int getPriority () {
		return this.priority;
	}
	
	/**
	 * Getter for this Requirement's estimate.
	 * @return the estimate
	 */
	public String getEstimate() {
		return this.estimate;
	}
	
	/**
	 * Getter for this Requirement's rejection reason as a Rejection object.
	 * @return the Rejection object
	 */
	public Rejection getRejectionReason() {
		return this.reason;
	}
	
	/**
	 * Getter for this Requirement's rejection reason as a String.
	 * @return the rejection reason
	 */
	public String getRejectionReasonString() {
		return reason.name();
	}
	
	/**
	 * Setter for this Requirement's rejection reason.
	 * @param reason the rejection reason
	 */
	private void setRejectionReason(String r) {
/*		if(r == null) {
			this.reason = NULL;
		}*/
		this.reason = Rejection.valueOf(r);
	}
	
	/**
	 * Getter for this Requirement's developer
	 * @return the developer
	 */
	public String getDeveloper() {
		return this.developer;
	}
	
	/**
	 * Getter for this Requirement's summary
	 * @return the summary
	 */
	public String getSummary() {
		return this.summary;
	}
	
	/**
	 * Getter for this Requirement's acceptance test id.
	 * @return the acceptance test id
	 */
	public String getAcceptanceTestId() {
		return this.acceptanceTestId;
	}
	
	/**
	 * Setter for this Requirement's acceptance test id.
	 * @param id the acceptance test id
	 */
	public void setAcceptanceTestId(String id) throws IllegalArgumentException {
		if (id == null) {
			throw new IllegalArgumentException("null id");
		}
		this.acceptanceTestId = id;
	}
	
	/**
	 * Setter for this Requirement's developer
	 * @param developer the developer
	 */
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	
	/**
	 * Updates this Requirement based on the specified command.
	 * @param command the command
	 * @throws UnsupportedOperationException if the Command is invalid for the state
	 */
	public void update(Command command) throws UnsupportedOperationException {
		this.state.updateState(command);
	}
	
	/**
	 * Getter for the Req object for a Requirement.
	 * @return the Req object
	 */
	public Req getXMLReq() {
		Req result = new Req();
		if (summary != null) {
		result.setSummary(this.summary);
		}
		if (acceptanceTestId != null) {
			result.setTest(this.acceptanceTestId);			
		}
		if (developer != null) {
			result.setDeveloper(this.developer);			
		}
		if (estimate != null) {		
		result.setEstimate(this.estimate);
		}
/*		if (requirementId != null) {			
		result.setId(this.requirementId);
		}
		if (priority != null) {
			result.setPriority(this.priority);
		} */
		if (reason != null) {		
		result.setRejection(this.getRejectionReasonString());
		}
		if (state != null) {
			result.setState(this.getState().getStateName());					
		}
		return result;
	}
	
	/**
	 * Setter for the counter variable.
	 * @param x the value to set
	 */
	public static void setCounter(int x) {
		counter = x;
	}
	
	/**
	 * Private inner (non-static) class representing the Accepted state.
	 * Implements the RequirementState interface and contains behavior to update state when given a Command and get its state name..
	 * If an UnsupportedOperationException is thrown, it should be passed all the way back to the GUI and 
	 */
	private class AcceptedState implements RequirementState {

		/**
		 * Updates Accepted state based on the Command.  
		 * @param c the Command
		 * @throws UnsupportedOperationException if the Command is invalid for this state
		 */
		@Override
		public void updateState(Command c) throws UnsupportedOperationException {
			if (c.getCommand() != CommandValue.REJECT && c.getCommand() != CommandValue.ASSIGN) {
				throw new UnsupportedOperationException("UnsupportedOperationException");
			}
			if (c.getCommand() == CommandValue.ASSIGN) {
				setDeveloper(c.getDeveloperId());
				setState(WORKING_NAME);
			}
			if (c.getCommand() == CommandValue.REJECT) {
				estimate = null;
				priority = 0;
				setDeveloper(null);
				setRejectionReason(c.getRejectionReason().name());
				setState(REJECTED_NAME);
			}
		}
		
		/**
		 * Getter for the state name as a String.
		 * @return the name
		 */
		@Override
		public String getStateName() {
			return ACCEPTED_NAME;
		}
	}
	
	/**
	 * Private inner (non-static) class representing the Completed state.
	 * Implements the RequirementState interface and contains behavior to update state when given a Command and get its state name..
	 * If an UnsupportedOperationException is thrown, it should be passed all the way back to the GUI and 
	 */
	private class CompletedState implements RequirementState {

		/**
		 * Updates Completed state based on the Command.  
		 * @param c the Command
		 * @throws UnsupportedOperationException if the Command is invalid for this state
		 */
		@Override
		public void updateState(Command c) throws UnsupportedOperationException {
			if (c.getCommand() != CommandValue.REJECT && c.getCommand() != CommandValue.PASS && c.getCommand() != CommandValue.FAIL && c.getCommand() != CommandValue.ASSIGN) {
				throw new UnsupportedOperationException("UnsupportedOperationException");
			}
			if (c.getCommand() == CommandValue.PASS) {
				setState(VERIFIED_NAME);
			}
			if (c.getCommand() == CommandValue.FAIL) {
				setState(WORKING_NAME);
			}
			if (c.getCommand() == CommandValue.ASSIGN) {
				setDeveloper(c.getDeveloperId());
				setState(WORKING_NAME);
			}
			if (c.getCommand() == CommandValue.REJECT) {
				estimate = null;
				priority = 0;
				setDeveloper(null);
				setRejectionReason(c.getRejectionReason().name());
				setState(REJECTED_NAME);
			}

		}
		
		/**
		 * Getter for the state name as a String.
		 * @return the name
		 */
		@Override
		public String getStateName() {
			return COMPLETED_NAME;
		}
	}
	
	/**
	 * Private inner (non-static) class representing the Rejected state.
	 * Implements the RequirementState interface and contains behavior to update state when given a Command and get its state name..
	 * If an UnsupportedOperationException is thrown, it should be passed all the way back to the GUI and 
	 */
	private class RejectedState implements RequirementState {

		/**
		 * Constructor for RejectedState.  
		 */
		private RejectedState() {

		}

		/**
		 * Updates Rejected state based on the Command.  
		 * @param c the Command
		 * @throws UnsupportedOperationException if the Command is invalid for this state
		 */
		@Override
		public void updateState(Command c) throws UnsupportedOperationException {
			if (c.getCommand() != CommandValue.REVISE) {
				throw new UnsupportedOperationException("UnsupportedOperationException");
			}
			summary = c.getSummary();
			setAcceptanceTestId(c.getAcceptanceTestId());
			//setRejectionReason(null);
			setState(SUBMITTED_NAME);
		}
		
		/**
		 * Getter for the state name as a String.
		 * @return the name
		 */
		@Override
		public String getStateName() {
			return REJECTED_NAME;
		}
	}
	
	/**
	 * Private inner (non-static) class representing the Submitted state.
	 * If an UnsupportedOperationException is thrown, it should be passed all the way back to the GUI and 
	 */
	private class SubmittedState implements RequirementState {

		/**
		 * Updates SubmittedState based on the Command.  
		 * @param c the Command
		 * @throws UnsupportedOperationException if the command is not Accept or Reject
		 */
		@Override
		public void updateState(Command c) throws UnsupportedOperationException {
			if (c.getCommand() != CommandValue.REJECT && c.getCommand() != CommandValue.ACCEPT) {
				throw new UnsupportedOperationException("UnsupportedOperationException");
			}					
			if (c.getCommand() == CommandValue.ACCEPT) {
				priority = c.getPriority();
				estimate = c.getEstimate();
				setState(ACCEPTED_NAME);
			}
			if (c.getCommand() == CommandValue.REJECT) {
				estimate = null;
				priority = 0;
				setDeveloper(null);
				setRejectionReason(c.getRejectionReason().name());
				setState(REJECTED_NAME);
			}
		}
		
		/**
		 * Getter for the state name as a String.
		 * @return the name
		 */
		@Override
		public String getStateName() {
			return SUBMITTED_NAME;
		}
	}
	
	/**
	 * Private inner (non-static) class representing the Verified state.
	 * Implements the RequirementState interface and contains behavior to update state when given a Command and get its state name..
	 * If an UnsupportedOperationException is thrown, it should be passed all the way back to the GUI and 
	 */
	private class VerifiedState implements RequirementState {

		/**
		 * Constructor for VerifiedState.  
		 */
		private VerifiedState() {

		}

		/**
		 * Updates state based on the Command.  
		 * @param c the Command
		 * @throws UnsupportedOperationException if the Command is invalid for this state
		 */
		@Override
		public void updateState(Command c) throws UnsupportedOperationException {
			if (c.getCommand() != CommandValue.REJECT && c.getCommand() != CommandValue.ASSIGN) {
				throw new UnsupportedOperationException("UnsupportedOperationException");
			}
			if (c.getCommand() == CommandValue.ASSIGN) {
				setDeveloper(c.getDeveloperId());
				setState(WORKING_NAME);
			}
			if (c.getCommand() == CommandValue.REJECT) {
				estimate = null;
				priority = 0;
				setDeveloper(null);
				setRejectionReason(c.getRejectionReason().name());
				setState(REJECTED_NAME);
			}

		}
		
		/**
		 * Getter for the state name as a String.
		 * @return the name
		 */
		@Override
		public String getStateName() {
			return VERIFIED_NAME;
		}
	}
	
	/**
	 * Private inner (non-static) class representing the Workng state.
	 * Implements the RequirementState interface and contains behavior to update state when given a Command and get its state name..
	 * If an UnsupportedOperationException is thrown, it should be passed all the way back to the GUI and 
	 */
	private class WorkingState implements RequirementState {

		/**
		 * Constructor for WorkingState.  
		 */
		private WorkingState() {

		}

		/**
		 * Updates state based on the Command.  
		 * @param c the Command
		 * @throws UnsupportedOperationException if the Command is invalid for this state
		 */
		@Override
		public void updateState(Command c) throws UnsupportedOperationException {
			if (c.getCommand() != CommandValue.REJECT && c.getCommand() != CommandValue.COMPLETE) {
				throw new UnsupportedOperationException("UnsupportedOperationException");
			}
			if (c.getCommand() == CommandValue.COMPLETE) {
				setState(COMPLETED_NAME);
			}
			if (c.getCommand() == CommandValue.REJECT) {
				estimate = null;
				priority = 0;
				setDeveloper(null);
				setRejectionReason(c.getRejectionReason().name());
				setState(REJECTED_NAME);
			}

		}
		
		/**
		 * Getter for the state name as a String.
		 * @return the name
		 */
		@Override
		public String getStateName() {
			return WORKING_NAME;
		}
	}
	
}

