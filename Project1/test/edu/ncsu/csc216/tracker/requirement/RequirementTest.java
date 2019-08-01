/**
 * 
 */
package edu.ncsu.csc216.tracker.requirement;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.ncsu.csc216.tracker.requirement.enums.CommandValue;
import edu.ncsu.csc216.tracker.requirement.enums.Rejection;
import edu.ncsu.csc216.tracker.xml.Req;

/**
 * Test class for Requirement class
 * @author mlee25 Michael Lee
 *
 */
public class RequirementTest {

	/**
	 * Test method for Requirement constructor taking String,String
	 */
	@Test
	public void testRequirementStringString() {
		Requirement requirement = null;

		//Test null summary
		try {
			requirement = new Requirement(null, "id");
		} catch (IllegalArgumentException e) {
			assertTrue(requirement == null);
		}
		
		//Test null acceptanceTestId
		try {
			requirement = new Requirement("summary", null);
		} catch (IllegalArgumentException e) {
			assertTrue(requirement == null);
		}
		
		//Test valid requirement
		try {
			requirement = new Requirement("summary", "id");
			assertEquals("summary", requirement.getSummary());
			assertEquals("id", requirement.getAcceptanceTestId());
		} catch (IllegalArgumentException e) {
			// no exception
		}		
	}

	/**
	 * Test method for Requirement constructor taking Req object
	 */
	@Test
	public void testRequirementReq() {
		Req r = new Req();
		r.setSummary("summary");
		r.setTest("acceptanceTestId");

		Requirement requirement = new Requirement(r);
		assertEquals("summary", requirement.getSummary());
		assertEquals("acceptanceTestId", requirement.getAcceptanceTestId());
			try {
		} catch (Exception e) {
			// should not throw an exception
		}
	}

	/**
	 * Test method for getState
	 */
	@Test
	public void testGetState() {
		Requirement requirement = new Requirement("summary", "id");
		assertEquals("summary", requirement.getSummary());
		assertEquals("id", requirement.getAcceptanceTestId());
		
		//Test new requirement is in initial state
		assertEquals(requirement.getState().getStateName(), "Submitted");
	}

	/**
	 * Test method for getRejectionReason
	 */
	@Test
	public void testGetRejectionReason() {
		Requirement requirement = new Requirement("summary", "id");
		assertEquals("summary", requirement.getSummary());
		assertEquals("id", requirement.getAcceptanceTestId());		
		assertEquals(requirement.getState().getStateName(), "Submitted");
		//Test valid REJECT 
		Command c = new Command(CommandValue.REJECT, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		Rejection r = requirement.getRejectionReason();
		assertEquals(Rejection.DUPLICATE, r);
		// assertEquals(requirement.getRejectionReasonString(), "DUPLICATE");
	}

	/**
	 * Test method for getRejectionReasonString
	 */
	@Test
	public void testGetRejectionReasonString() {
		Requirement requirement = new Requirement("summary", "id");
		assertEquals("summary", requirement.getSummary());
		assertEquals("id", requirement.getAcceptanceTestId());		
		assertEquals(requirement.getState().getStateName(), "Submitted");
		//Test valid REJECT 
		Command c = new Command(CommandValue.REJECT, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		assertEquals("DUPLICATE", requirement.getRejectionReasonString());
	}

	/**
	 * Test method for getXMLReq
	 */
	@Test
	public void testGetXMLReq() {
		Req r = new Req();
		r.setSummary("summary");
		r.setTest("id");

		Requirement requirement = new Requirement(r);

		r = requirement.getXMLReq();
		assertEquals("summary", r.getSummary());
		assertEquals("id", r.getTest());		
	}

	/**
	 * Test method for setCounter
	 */
	@Test
	public void testSetCounter() {
		Requirement.setCounter(0);
		Requirement requirement = new Requirement("summary", "id");
		assertEquals("summary", requirement.getSummary());
		assertEquals("id", requirement.getAcceptanceTestId());		
		assertEquals(requirement.getState().getStateName(), "Submitted");
		//test requirement is assigned and increments properly
		assertEquals(0, requirement.getRequirementId());
		Requirement requirement2 = new Requirement("summary", "id");
		assertEquals(1, requirement2.getRequirementId());
		Requirement requirement3 = new Requirement("summary", "id");
		assertEquals(2, requirement3.getRequirementId());
		//set requirement to 10
		Requirement.setCounter(10);
		Requirement requirement10 = new Requirement("summary", "id");
		assertEquals(10, requirement10.getRequirementId());
		Requirement requirement11 = new Requirement("summary", "id");
		assertEquals(11, requirement11.getRequirementId());
	}
	
	/**
	 * Test method for update for Submitted state
	 */
	@Test
	public void testUpdateSubmitted() {
		Requirement requirement = null;
		Command c = null;

		requirement = new Requirement("summary", "id");
		assertEquals("summary", requirement.getSummary());
		assertEquals("id", requirement.getAcceptanceTestId());
		
		//Confirm initial state is Submitted
		assertEquals(requirement.getState().getStateName(), "Submitted");

		//Test null command 
		try {
			c = new Command(null, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test ACCEPT with null estimate
		try {
			c = new Command(CommandValue.ACCEPT, "summary", "id", 1, null, "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test ACCEPT with empty estimate
		try {
			c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test ACCEPT with priority < 1
		try {
			c = new Command(CommandValue.ACCEPT, "summary", "id", 0, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test ACCEPT with priority > 3
		try {
			c = new Command(CommandValue.ACCEPT, "summary", "id", 4, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test valid ACCEPT 
		c = new Command(CommandValue.ACCEPT, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		assertEquals(requirement.getState().getStateName(), "Accepted");

		//Test REJECT with null rejection
		try {
			c = new Command(CommandValue.REJECT, "summary", "id", 1, "estimate", "developer", null);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test valid REJECT 
		c = new Command(CommandValue.REJECT, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		assertEquals(requirement.getState().getStateName(), "Rejected");
		assertEquals(requirement.getRejectionReasonString(), "DUPLICATE");
	}

	/**
	 * Test method for update for Accepted state
	 */
	@Test
	public void testUpdateAccepted() {
		Requirement requirement = null;
		Req r = null;
		Command c = null;

		requirement = new Requirement("summary", "id");
		//ACCEPT 
		c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = new Req();
		r = requirement.getXMLReq();
		assertEquals(Requirement.ACCEPTED_NAME, r.getState());

		//Test null command 
		try {
			c = new Command(null, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		
		//Test unsupported command 
		try {
			c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			assertEquals("UnsupportedOperationException", e.getMessage());
		}
		
		//Test ASSIGN with null developer
		try {
			c = new Command(CommandValue.ASSIGN, "summary", "id", 1, "estimate", null, Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//Test ASSIGN with empty developer
		try {
			c = new Command(CommandValue.ASSIGN, "summary", "id", 1, "estimate", "", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}

		//ASSIGN with valid developer
		c = new Command(CommandValue.ASSIGN, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.WORKING_NAME, r.getState());
		assertEquals("summary", r.getSummary());
		assertEquals("id", r.getTest());
		assertEquals("developer", r.getDeveloper());
		
		//Test valid REJECT 
		c = new Command(CommandValue.REJECT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals("Rejected", r.getState());
		assertEquals("DUPLICATE", r.getRejection());
		assertEquals(null, r.getEstimate());
		assertEquals(0, r.getPriority());
		assertEquals(null, r.getDeveloper());
	}

	/**
	 * Test method for update for Working state
	 */
	@Test
	public void testUpdateWorking() {
		Requirement requirement = null;
		Req r = new Req();
		Command c = null;
		requirement = new Requirement("summary", "id");

		//ACCEPT 
		c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.ACCEPTED_NAME, r.getState());

		//ASSIGN
		c = new Command(CommandValue.ASSIGN, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.WORKING_NAME, r.getState());
		
		//Test null command 
		try {
			c = new Command(null, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		
		//Test unsupported command 
		try {
			c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			assertEquals("UnsupportedOperationException", e.getMessage());
		}
		
		//test valid COMPLETE 
		c = new Command(CommandValue.COMPLETE, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.COMPLETED_NAME, r.getState());
		assertEquals("summary", r.getSummary());
		assertEquals("id", r.getTest());
		assertEquals("developer", r.getDeveloper());
		
		//Test valid REJECT 
		c = new Command(CommandValue.REJECT, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals("Rejected", r.getState());
		assertEquals("DUPLICATE", r.getRejection());
		assertEquals(null, r.getEstimate());
		assertEquals(0, r.getPriority());
		assertEquals(null, r.getDeveloper());
	}

	/**
	 * Test method for update for Completed state
	 */
	@Test
	public void testUpdateCompleted() {
		Requirement requirement = null;
		Req r = new Req();
		Command c = null;
		requirement = new Requirement("summary", "id");

		//ACCEPT (transition to ACCEPTED)
		c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.ACCEPTED_NAME, r.getState());

		//ASSIGN (transition to WORKING)
		c = new Command(CommandValue.ASSIGN, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.WORKING_NAME, r.getState());
		
		//COMPLETE (transition to COMPLETED)
		c = new Command(CommandValue.COMPLETE, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.COMPLETED_NAME, r.getState());

		//Test null command 
		try {
			c = new Command(null, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		
		//Test unsupported command 
		try {
			c = new Command(CommandValue.COMPLETE, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			assertEquals("UnsupportedOperationException", e.getMessage());
		}
		
		//Test REJECT 
		c = new Command(CommandValue.REJECT, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals("Rejected", r.getState());
		assertEquals("DUPLICATE", r.getRejection());
		assertEquals(null, r.getEstimate());
		assertEquals(0, r.getPriority());
		assertEquals(null, r.getDeveloper());

		//test valid PASS 
		requirement = new Requirement("summary", "id");
		c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		c = new Command(CommandValue.ASSIGN, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		c = new Command(CommandValue.COMPLETE, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		c = new Command(CommandValue.PASS, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.VERIFIED_NAME, r.getState());
		assertEquals("summary", r.getSummary());
		assertEquals("id", r.getTest());
		assertEquals("developer", r.getDeveloper());

		//test valid FAIL
		requirement = new Requirement("summary", "id");
		c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		c = new Command(CommandValue.ASSIGN, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		c = new Command(CommandValue.COMPLETE, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}

		c = new Command(CommandValue.FAIL, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.WORKING_NAME, r.getState());
		assertEquals("summary", r.getSummary());
		assertEquals("id", r.getTest());
		assertEquals("developer", r.getDeveloper());
		
		// TEST ASSIGN
		requirement = new Requirement("summary", "id");
		c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		c = new Command(CommandValue.ASSIGN, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		c = new Command(CommandValue.COMPLETE, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}

		//null developer
		try {
			c = new Command(CommandValue.ASSIGN, "summary", "id", 1, "estimate", null, Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//empty developer
		try {
			c = new Command(CommandValue.ASSIGN, "summary", "id", 1, "estimate", "", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//ASSIGN with valid developer
		c = new Command(CommandValue.ASSIGN, "summary", "id", 1, "estimate", "newdeveloper", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.WORKING_NAME, r.getState());
		assertEquals("summary", r.getSummary());
		assertEquals("id", r.getTest());
		assertEquals("newdeveloper", r.getDeveloper());	
	}

	/**
	 * Test method for update for Verified state
	 */
	@Test
	public void testUpdateVerified() {
		Requirement requirement = null;
		Req r = new Req();
		Command c = null;
		requirement = new Requirement("summary", "id");

		//ACCEPT (transition to ACCEPTED)
		c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.ACCEPTED_NAME, r.getState());

		//ASSIGN (transition to WORKING)
		c = new Command(CommandValue.ASSIGN, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.WORKING_NAME, r.getState());
		
		//COMPLETE (transition to COMPLETED)
		c = new Command(CommandValue.COMPLETE, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.COMPLETED_NAME, r.getState());

		//PASS (transition to VERIFIED)
		c = new Command(CommandValue.PASS, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.VERIFIED_NAME, r.getState());
		assertEquals("summary", r.getSummary());
		assertEquals("id", r.getTest());
		assertEquals("developer", r.getDeveloper());

		//Test null command 
		try {
			c = new Command(null, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		
		//Test unsupported command 
		try {
			c = new Command(CommandValue.COMPLETE, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			assertEquals("UnsupportedOperationException", e.getMessage());
		}
		
		//Test REJECT 
		c = new Command(CommandValue.REJECT, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals("Rejected", r.getState());
		assertEquals("DUPLICATE", r.getRejection());
		assertEquals(null, r.getEstimate());
		assertEquals(0, r.getPriority());
		assertEquals(null, r.getDeveloper());

		// TEST ASSIGN
		requirement = new Requirement("summary", "id");
		c = new Command(CommandValue.ACCEPT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		c = new Command(CommandValue.ASSIGN, "summary", "id", 2, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		c = new Command(CommandValue.COMPLETE, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		c = new Command(CommandValue.PASS, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}

		//null developer
		try {
			c = new Command(CommandValue.ASSIGN, "summary", "id", 1, "estimate", null, Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//empty developer
		try {
			c = new Command(CommandValue.ASSIGN, "summary", "id", 1, "estimate", "", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		//ASSIGN with valid developer
		c = new Command(CommandValue.ASSIGN, "summary", "id", 1, "estimate", "newdeveloper", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.WORKING_NAME, r.getState());
		assertEquals("summary", r.getSummary());
		assertEquals("id", r.getTest());
		assertEquals("newdeveloper", r.getDeveloper());
	}
	
	/**
	 * Test method for update for Rejected state
	 */
	@Test
	public void testUpdateRejected() {
		Requirement requirement = null;
		Req r = new Req();
		Command c = null;
		requirement = new Requirement("summary", "id");

		//REJECT (transition to REJECTED
		c = new Command(CommandValue.REJECT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals("Rejected", r.getState());
		assertEquals("DUPLICATE", r.getRejection());
		assertEquals(null, r.getEstimate());
		assertEquals(0, r.getPriority());
		assertEquals(null, r.getDeveloper());

		//Test null command 
		try {
			c = new Command(null, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command", e.getMessage());
		}
		
		//Test unsupported command 
		try {
			c = new Command(CommandValue.REJECT, "summary", "id", 1, "estimate", "developer", Rejection.DUPLICATE);
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			assertEquals("UnsupportedOperationException", e.getMessage());
		}
		
		//Test Revise 
		c = new Command(CommandValue.REVISE, "newsummary", "newid", 1, "estimate", "developer", Rejection.DUPLICATE);
		try {
			requirement.update(c);
		} catch (UnsupportedOperationException e) {
			// valid
		}
		r = requirement.getXMLReq();
		assertEquals(Requirement.SUBMITTED_NAME, r.getState());
		assertEquals("newsummary", r.getSummary());
		assertEquals("newid", r.getTest());
		assertEquals("DUPLICATE", r.getRejection());
	}
}
