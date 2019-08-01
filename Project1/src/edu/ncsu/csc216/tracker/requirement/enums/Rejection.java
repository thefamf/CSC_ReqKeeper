/**
 * 
 */
package edu.ncsu.csc216.tracker.requirement.enums;

/**
 * Enumeration that names the possible reasons for rejection of a requirement.
 * @author mlee25 Michael Lee
 */
public enum Rejection {

	/** Rejection reason: duplicates existing requirement */  
	DUPLICATE,  

	/** Rejection reason: infeasible */  
	INFEASIBLE,  

	/** Rejection reason: too large */  
	TOO_LARGE,  

	/** Rejection reason: out of scope */  
	OUT_OF_SCOPE,  

	/** Rejection reason: inappropriate */  
	INAPPROPRIATE,  
}
