package com.Tester.BuchLadenTester.Service;


import com.Tester.BuchLadenTester.Model.User;

public interface UserService {

	User saveUser(User user);
	
	boolean isUserWithEmailAlreadyPresent(User user);

	User findByUsername(String username);
}
