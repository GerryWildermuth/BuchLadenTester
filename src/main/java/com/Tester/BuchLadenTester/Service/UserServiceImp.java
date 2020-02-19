package com.Tester.BuchLadenTester.Service;


import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Repository.UserRepository;
import org.springframework.stereotype.Service;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.bCryptPasswordEncoder;


@Service
public class UserServiceImp implements UserService {

	final
	UserRepository userRepository;

	public UserServiceImp(UserRepository userRepository) {

		this.userRepository = userRepository;
	}

	public User saveUser(User user)
	{
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		//requireNonNull(user.getEmail());//prove if email exists before persistant call
		userRepository.save(user);
		return user;
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
