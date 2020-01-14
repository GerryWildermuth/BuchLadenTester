package com.Tester.BuchLadenTester.Service;


import com.Tester.BuchLadenTester.Model.Role;
import com.Tester.BuchLadenTester.Repository.RoleRepository;
import com.Tester.BuchLadenTester.Repository.UserRepository;
import com.Tester.BuchLadenTester.Model.User;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.bCryptPasswordEncoder;


@Service
public class UserServiceImp implements UserService {
	

	final
	RoleRepository roleRepository;
	final
	UserRepository userRepository;

	public UserServiceImp(RoleRepository roleRepository, UserRepository userRepository) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}

	@Override
	public void saveUser(User user)
	{
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}

	@Override
	public boolean isUserAlreadyPresent(User user) {
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
