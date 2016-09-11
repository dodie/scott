package hu.advancedweb.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UserTest {
	
	@Test
	public void test_1() {
		UserService service = new UserService();
		
		User john = service.createUser("john@doe", "John Doe");
		User jane = service.createUser("jane@doe", "Jane Doe");
		
		assertEquals(john.getId(), jane.getId());
	}
	
}
