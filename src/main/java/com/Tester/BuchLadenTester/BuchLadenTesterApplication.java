package com.Tester.BuchLadenTester;

import com.Tester.BuchLadenTester.Enums.Enums;
import com.Tester.BuchLadenTester.Model.*;
import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Repository.PrivilegesRepository;
import com.Tester.BuchLadenTester.Repository.RoleRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Service.UserServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BuchLadenTesterApplication {
	@Autowired
	private UserServiceImp UserDetails;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegesRepository privilegesRepository;

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private ShoppingcartRepository shoppingcartRepository;

	public static Logger logger = LoggerFactory.getLogger("myStudyLogger");
	public static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	public static void main(String[] args) {
		SpringApplication.run(BuchLadenTesterApplication.class, args);
	}



	@Bean
	InitializingBean sendDatabase() {
		return () -> {
			String Admin = Enums.Role.ADMIN.toString();
			String User = Enums.Role.USER.toString();
			Role RoleAdmin = new Role(Admin,Admin,Admin);
			Role RoleUser = new Role(User,User,User);

			roleRepository.saveAndFlush(RoleAdmin);
			roleRepository.saveAndFlush(RoleUser);

			Role AdminRole = roleRepository.findByRole(Admin);

			Privilege adminPrivilege =  privilegesRepository.findByName(User);
			com.Tester.BuchLadenTester.Model.User RegularUser = new User("gerry1313@web.de","12345","Gerry",Admin);
			UserDetails.saveUser(RegularUser);

			Calendar calendar = Calendar.getInstance();

			Date date = new Date(calendar.getTime().getTime());
			Book Book1 = new Book("Horizon",date,"",5.0);
			Book Book2 = new Book("A Book",date,"",5.0);
			Book Book3 = new Book("NEW",date,"",5.0);
			bookRepository.save(Book1);
			bookRepository.save(Book2);
			bookRepository.save(Book3);

			Set<Book> bookSet = new HashSet<>();
			bookSet.add(Book1);
			bookSet.add(Book2);
			bookSet.add(Book3);
			Shoppingcart shoppingcart = RegularUser.getShoppingcart();
			shoppingcart.setBooks(bookSet);
			shoppingcartRepository.save(shoppingcart);
		};
	}
}
