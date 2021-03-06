package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Model.Role;
import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Repository.RoleRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Repository.UserRepository;
import com.Tester.BuchLadenTester.Service.SecurityServiceImpl;
import com.Tester.BuchLadenTester.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.logger;

@Controller()
public class AuthenticationController  {
	final
	UserService userService;
	final
	RoleRepository roleRepository;

	final UserRepository userRepository;

	final SecurityServiceImpl securityService;

	final
	ShoppingcartRepository shoppingcartRepository;

	final
	BookRepository bookRepository;

	//Init of all services and repositories
	public AuthenticationController(UserService userService, RoleRepository roleRepository, UserRepository userRepository, SecurityServiceImpl securityService, ShoppingcartRepository shoppingcartRepository, BookRepository bookRepository) {
		this.userService = userService;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.securityService = securityService;
		this.shoppingcartRepository = shoppingcartRepository;
		this.bookRepository = bookRepository;
	}

	//Login Post is handled automatically
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
		modelAndView.setViewName("admin");
		return modelAndView;
	}

	@GetMapping(value = "/register")
	public ModelAndView register() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user", new User());
		modelAndView.addObject("userRoles",getRoleStrings());
		modelAndView.setViewName("register");
		return modelAndView;
	}

	@PostMapping(value="/register")
	public ModelAndView registerUser(@Valid User user, BindingResult bindingResult, ModelMap modelMap) {
		ModelAndView modelAndView = new ModelAndView();
		//If Post is called without previous get call creat a new user
		if(user!=null)
			modelAndView.addObject("user", user);
		else
			modelAndView.addObject("user", new User());

		modelAndView.addObject("userRoles", getRoleStrings());
		modelAndView.setViewName("register");

		if(bindingResult.hasErrors()) {
			modelAndView.addObject("successMessage", "Please correct the errors in form!");
			logger.info("Please correct the errors in form!");
			modelMap.addAttribute("bindingResult", bindingResult);
		}
		else if(userService.isUserWithEmailAlreadyPresent(user)){
			assert user != null;
			modelAndView.addObject("successMessage", "user with that email already exists! email: "+user.getEmail());
			logger.info("user with that email already exists! email: "+user.getEmail());
		}
		else {
			assert user != null;
			if(user.getUserRoles().isEmpty()){
				modelAndView.addObject("successMessage", "User needs to have a Role");
				logger.info("User needs to have a Role");
			}
			else {
				String notEncryptedPassword = user.getPassword();
				userService.saveUser(user);
				securityService.autoLogin(user.getEmail(), notEncryptedPassword);
				modelAndView.addObject("successMessage", "User is registered successfully! with name: "+user.getName());
				logger.info("User is registered successfully! with name: "+user.getName());
				modelAndView.addObject("books",bookRepository.findAll());
				modelAndView.setViewName("books");
			}
		}
		return modelAndView;
	}
	//InitBinder is needed to resolve roleString from Form into actuall role objects
	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "userRoles", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				if (element instanceof Role) {
					System.out.println("Converting from Role to Role: " + element);
					return element;
				}
				if (element instanceof String) {
					List<Role> roles =roleRepository.findAll();
					Optional<Role> firstRoleToFind = roles.stream().filter(role -> role.getRole().equals(element.toString())).findFirst();
					if(firstRoleToFind.isPresent()) {
						System.out.println("Looking up roles for String " + element + ": " + firstRoleToFind.get().getRole());
						return firstRoleToFind.get();
					}
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










