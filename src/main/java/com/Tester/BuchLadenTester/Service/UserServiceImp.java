package com.Tester.BuchLadenTester.Service;


import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Repository.RoleRepository;
import com.Tester.BuchLadenTester.Repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.bCryptPasswordEncoder;


@Service
public class UserServiceImp implements UserService {
	

	final
	RoleRepository roleRepository;
	final
	UserRepository userRepository;
	final HttpServletRequest request;

	public UserServiceImp(RoleRepository roleRepository, UserRepository userRepository, HttpServletRequest request) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.request = request;
	}

	public void saveUser(User user)
	{
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}


	public boolean isUserWithEmailAlreadyPresent(User user) {
		boolean isUserAlreadyExists = false;
		User existingUser = userRepository.findByEmail(user.getEmail());
		// If user is found in database, then then user already exists.
		if(existingUser != null){
			isUserAlreadyExists = true;
		}
		return isUserAlreadyExists;
	}
	@Override
	public User findByUsername(String username) {
		return userRepository.findByName(username);
	}

}
