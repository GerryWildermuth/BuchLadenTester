package com.Tester.BuchLadenTester.Service;


import com.Tester.BuchLadenTester.Model.User;

public interface UserService {

	void saveUser(User user);
	
	boolean isUserAlreadyPresent(User user);

	User findByUsername(String username);
}
