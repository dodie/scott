package hu.advancedweb.example;

public class UserService {
	
	private Long lastId = 41L;
	
	public User createUser(String email, String name) {
		User user;
		user = new User();
		user.setId(++lastId);
		user.setEmail(email);
		user.setName(name);
		return user;
	}
	
}
