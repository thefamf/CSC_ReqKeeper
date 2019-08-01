/**
 * 
 */
package edu.ncsu.csc216.tracker.requirement.enums;

/**
 * Enumeration that names the commands that the user gives in the GUI for processing by the internal FSM.
 * @author mlee25 Michael Lee
 */  
 public enum CommandValue {  
   
 /** Accepted requirement: Submitted to Accepted */  
 ACCEPT,  
 
 /** Rejected requirement: Any to Rejected */  
 REJECT,  
 
 /** Revised requirement: Rejected to Submitted */  
 REVISE,  
 
 /** Assigned requirement: Accepted to Working, Completed to Working, Verified to Working */  
 ASSIGN,  
 
 /** Completed requirement: Working to Completed */  
 COMPLETE,  
 
 /** Verified requirement: Completed to Verified */  
 PASS,  
 
 /** Failing requirement: Completed to Working */  
 FAIL
 
}