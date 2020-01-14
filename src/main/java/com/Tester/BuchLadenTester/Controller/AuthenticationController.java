package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.Repository.RoleRepository;
import com.Tester.BuchLadenTester.Repository.UserRepository;
import com.Tester.BuchLadenTester.Service.SecurityServiceImpl;
import com.Tester.BuchLadenTester.Model.Role;
import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Service.UserService;
import groovy.lang.Grab;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.logger;

@Grab("thymeleaf-spring4")
@Controller()
public class AuthenticationController  {
//extends WebMvcConfigurerAdapter
	final
UserService userService;
	final
	RoleRepository roleRepository;
	final UserRepository userRepository;

	final SecurityServiceImpl securityService;

	private Map<String, Role> roleCache;

	public AuthenticationController(UserService userService, RoleRepository roleRepository, UserRepository userRepository, SecurityServiceImpl securityService) {
		this.userService = userService;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.securityService = securityService;
	}

	//Post is handelt automatically
	@GetMapping(value = { "/login" }, produces = {"application/json"})
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login"); // resources/template/login.html
		return modelAndView;
	}

	@GetMapping(value = "/home", produces = {"application/json"})
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home"); // resources/template/home.html
		return modelAndView;
	}
	
	@GetMapping(value = "/admin")
	public ModelAndView adminHome() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin"); // resources/template/admin.html
		return modelAndView;
	}

	@GetMapping(value = "/register")
	public ModelAndView register() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		List<Role> activeRole = roleRepository.findAll();
		roleCache = new HashMap<>();
		for (Role role : activeRole) {
			roleCache.put(role.getRole(), role);
		}
		modelAndView.addObject("user", user);
		modelAndView.addObject("roles",getRoleStrings());
		modelAndView.setViewName("register"); // resources/template/register.html
		return modelAndView;
	}

	@PostMapping(value="/register")
	public ModelAndView registerUser(@Valid User user, BindingResult bindingResult, ModelMap modelMap) {
		ModelAndView modelAndView = new ModelAndView();
		if(user!=null)
			modelAndView.addObject("user", user);
		else
			modelAndView.addObject("user", new User());
		modelAndView.addObject("roles", getRoleStrings());
		modelAndView.setViewName("register");
		// Check for the validations
		if(bindingResult.hasErrors()) {
			modelAndView.addObject("successMessage", "Please correct the errors in form!");
			logger.info("Please correct the errors in form!");

			modelMap.addAttribute("bindingResult", bindingResult);
		}
		else if(userService.isUserAlreadyPresent(user)){
			modelAndView.addObject("successMessage", "user already exists!");
			logger.info("user already exists!");
		}
		else if(roleRepository.findByRole("USER") == null){
			modelAndView.addObject("successMessage", "no Role for the User");
			logger.info("no Role for the User");
		}
		// we will save the user if, no binding errors
		else {
			userService.saveUser(user);
			assert user != null;
			securityService.autoLogin(user.getEmail(), user.getPassword());
			modelAndView.addObject("successMessage", "User is registered successfully!");
			logger.info("User is registered successfully!");
			modelAndView.setViewName("books");
		}


		return modelAndView;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "roles", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				if (element instanceof Role) {
					System.out.println("Converting from Role to Role: " + element);
					return element;
				}
				if (element instanceof String) {
					//Role role =  roleCache.get(element);
					Role role = roleRepository.findByRole(element.toString());
					System.out.println("Looking up role for String " + element + ": " + role);
					return role;
				}
				System.out.println("Don't know what to do with: " + element);
				return null;
			}
		});
	}
	private List<String> getRoleStrings() {
		List<String> roleStrings = new ArrayList<>();
		for (Role role :  roleRepository.findAll()) {
			roleStrings.add(role.getRole());
		}
		return roleStrings;
	}
}









