package edu.ncsu.csc216.tracker.requirement;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.ncsu.csc216.tracker.requirement.enums.CommandValue;
import edu.ncsu.csc216.tracker.requirement.enums.Rejection;

/**
 * Test class for Command class
 * @author mlee25 Michael Lee
 *
 */
public class CommandTest {

	/**
	 * Test method for Command constructor
	 */
	@Test
	public void testCommand() {
		Command c = null;
		try {
			c = new Command(null, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);			
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test ACCEPT with null estimate
		try {
			c = new Command(CommandValue.ACCEPT, "summary", "id", 1, null, "developer", Rejection.DUPLICATE);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test ACCEPT with empty estimate
		try {
			c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "", "developer", Rejection.DUPLICATE);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test ACCEPT with priority < 1
		try {
			c = new Command(CommandValue.ACCEPT, "summary", "id", 0, "estimate", "developer", Rejection.DUPLICATE);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test ACCEPT with priority > 3
		try {
			c = new Command(CommandValue.ACCEPT, "summary", "id", 4, "estimate", "developer", Rejection.DUPLICATE);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}

		//Test ASSIGN with null developer
		try {
			c = new Command(CommandValue.ASSIGN, "summary", "id", 1, "estimate", null, Rejection.DUPLICATE);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test ASSIGN with empty developer
		try {
			c = new Command(CommandValue.ASSIGN, "summary", "id", 1, "estimate", "", Rejection.DUPLICATE);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}

		//Test REVISE with null summary
		try {
			c = new Command(CommandValue.REVISE, null, "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}

		//Test REVISE with empty summary
		try {
			c = new Command(CommandValue.REVISE, "", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}

		//Test REVISE with null id 
		try {
			c = new Command(CommandValue.REVISE, "summary", null, 1, "estimate", "developer", Rejection.DUPLICATE);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}

		//Test REVISE with empty id 
		try {
			c = new Command(CommandValue.REVISE, "summary", "", 1, "estimate", "developer", Rejection.DUPLICATE);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}

		//Test REJECT with null rejection
		try {
			c = new Command(CommandValue.REJECT, "summary", "id", 1, "estimate", "developer", null);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test valid Reject
		c = new Command(CommandValue.REJECT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		assertEquals(CommandValue.REJECT, c.getCommand());
		assertEquals("summary", c.getSummary());
		assertEquals("id", c.getAcceptanceTestId());
		assertEquals(1, c.getPriority());
		assertEquals("estimate", c.getEstimate());
		assertEquals("developer", c.getDeveloperId());
		assertEquals(Rejection.DUPLICATE, c.getRejectionReason());
	}

	/**
	 * Test method for getCommandValue
	 */
	@Test
	public void testGetCommandValue() {
		Command c = new Command(CommandValue.REJECT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		assertEquals(CommandValue.REJECT, c.getCommand());
	}

	/**
	 * Test method for getRejectionReason
	 */
	@Test
	public void testGetRejectionReason() {
		Command c = new Command(CommandValue.REJECT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		assertEquals(Rejection.DUPLICATE, c.getRejectionReason());
	}

}
