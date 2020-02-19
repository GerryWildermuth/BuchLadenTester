package com.Tester.BuchLadenTester;

import com.Tester.BuchLadenTester.Enums.Enums;
import com.Tester.BuchLadenTester.Model.*;
import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Service.UserServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
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

	private final AuthorRepository authorRepository;

	private final ShoppingcartRepository shoppingcartRepository;

	private final UserServiceImp UserService;

	public static Logger logger = LoggerFactory.getLogger("myStudyLogger");
	public static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	public BuchLadenTesterApplication(AuthorRepository authorRepository, ShoppingcartRepository shoppingcartRepository, UserServiceImp UserService) {
		this.authorRepository = authorRepository;
		this.shoppingcartRepository = shoppingcartRepository;
		this.UserService = UserService;
	}

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

			User AdminUser = new User("admin@web.de","12345","admin",Admin);
			User RegularUser = new User("user@web.de","12345","user",User);
			RegularUser.getUserRoles().add(RoleUser);
			AdminUser.getUserRoles().add(RoleAdmin);
			AdminUser.getUserRoles().add(RoleUser);

			UserService.saveUser(RegularUser);
			UserService.saveUser(AdminUser);

			Calendar calendar = Calendar.getInstance();
			Date date = new Date(calendar.getTime().getTime());

			Author author1 = new Author("unknown","unknown",date);
			Book Book1 = new Book("Horizon",date,author1,"",5.0);
			Book Book2 = new Book("ABook",date,author1,"",7.0);
			Book Book3 = new Book("NEW",date,author1,"",5.0);
			Book Book4 = new Book("This",date,author1,"",50.0);
			Book Book5 = new Book("Hey",date,author1,"",15.0);
			author1.getAuthorBooks().add(Book1);
			author1.getAuthorBooks().add(Book2);
			author1.getAuthorBooks().add(Book3);
			author1.getAuthorBooks().add(Book4);
			author1.getAuthorBooks().add(Book5);
			authorRepository.save(author1);

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
