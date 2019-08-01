package edu.ncsu.csc216.tracker.requirement;


/**
 * Interface for states in the Requirement State Pattern.  All 
 * concrete requirement states must implement the RequirementState interface.
 * 
 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu) 
 */
public interface RequirementState {
	
	/**
	 * Update the {@link Requirement} based on the given {@link Command}.
	 * An {@link UnsupportedOperationException} is throw if the {@link CommandValue}
	 * is not a valid action for the given state.  
	 * @param c {@link Command} describing the action that will update the {@link Requirement}'s
	 * state.
	 * @throws UnsupportedOperationException if the {@link CommandValue} is not a valid action
	 * for the given state.
	 */
	void updateState(Command c);
	
	/**
	 * Returns the name of the current state as a String.
	 * @return the name of the current state as a String.
	 */
	String getStateName();

}
