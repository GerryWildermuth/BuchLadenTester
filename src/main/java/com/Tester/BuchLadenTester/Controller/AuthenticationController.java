package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Model.Shoppingcart;
import com.Tester.BuchLadenTester.Repository.RoleRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Repository.UserRepository;
import com.Tester.BuchLadenTester.Service.SecurityServiceImpl;
import com.Tester.BuchLadenTester.Model.Role;
import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Service.UserService;
import groovy.lang.Grab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
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

	final
	ShoppingcartRepository shoppingcartRepository;

	public AuthenticationController(UserService userService, RoleRepository roleRepository, UserRepository userRepository, SecurityServiceImpl securityService, ShoppingcartRepository shoppingcartRepository) {
		this.userService = userService;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.securityService = securityService;
		this.shoppingcartRepository = shoppingcartRepository;
	}

	//Post is handelt automatically
	@GetMapping(value = { "/login" }, produces = {"application/json"})
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login"); // resources/template/login.html
		return modelAndView;
	}

	@GetMapping(value = "/home")
	public ModelAndView home(Principal principal) {
		ModelAndView modelAndView = new ModelAndView();
		User currentUser = userRepository.findByEmail(principal.getName());
		Set<Book> books = currentUser.getUserBooks();
		modelAndView.addObject("books",books);
		modelAndView.setViewName("userBooks");
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
		modelAndView.addObject("user", new User());
		modelAndView.addObject("userRoles",getRoleStrings());
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
		else {
			assert user != null;
			if(user.getUserRoles().isEmpty()){
				modelAndView.addObject("successMessage", "User has no Role");
				logger.info("User has no Role");
			}
			// we will save the user if, no binding errors
			else {
				String tempPassword = user.getPassword();
				userService.saveUser(user);
				securityService.autoLogin(user.getEmail(), tempPassword);
				modelAndView.addObject("successMessage", "User is registered successfully!");
				logger.info("User is registered successfully!");

				modelAndView.setViewName("forward:/books");
			}
		}
		return modelAndView;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "userRoles", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				if (element instanceof Role) {
					System.out.println("Converting from Role to Role: " + element);
					return element;
				}
				if (element instanceof String) {
					//Role roles =  roleCache.get(element);
					Role role =roleRepository.findByRole(element.toString());
					System.out.println("Looking up roles for String " + element + ": " + role);
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










