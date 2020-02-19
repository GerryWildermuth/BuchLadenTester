package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.BuchLadenTesterApplication;
import com.Tester.BuchLadenTester.Model.Author;
import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Model.Shoppingcart;
import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BuchLadenTesterApplication.class,UserServiceImp.class})
@TestPropertySource(locations = {"classpath:application.properties","classpath:application-prod.properties","classpath:application-dev.properties"})
class ShoppingCartServiceImpTest {

    @TestConfiguration
    static class ShoppingCartServiceImpTestContextConfiguration {

        @Bean
        public ShoppingCartService shoppingCartService() {
            return new ShoppingCartServiceImp();
        }
    }

    @Autowired
    private ShoppingCartService shoppingCartService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;
    private Set<Book> bookSet = new HashSet<>();
    private Shoppingcart shoppingcart;
    private User AdminUser;
    @BeforeEach
    void setUp() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTime().getTime());
        AdminUser = new User("admin@web.de","12345","admin","Admin");
        Author author1 = new Author("test","tester",date);
        Book Book1 = new Book("test",date,author1,"",5.0);
        Book Book2 = new Book("ABook",date,author1,"",7.0);
        Book Book3 = new Book("NEWT",date,author1,"",5.0);
        Book Book4 = new Book("This",date,author1,"",50.0);
        Book Book5 = new Book("Hey",date,author1,"",15.0);
        bookSet.add(Book1);
        bookSet.add(Book2);
        bookSet.add(Book3);
        bookSet.add(Book4);
        bookSet.add(Book5);
        shoppingcart = new Shoppingcart(new HashSet<>(bookSet));
        AdminUser.setShoppingcart(shoppingcart);
    }
    @Test
    void totalPriceOfAllBooks() {
        double totalPrice = 0;
        double totalPricePreCalculated= 0;
        totalPricePreCalculated= bookSet.stream().mapToDouble(Book::getPrice).sum();
        totalPrice= shoppingcart.getBooks().stream().mapToDouble(Book::getPrice).sum();
        Assertions.assertThat(totalPrice==totalPricePreCalculated);
    }

    //whenBookIsPresentItShouldReturnTrue
    @Test
    void isBookAlreadyPresentInShoppingCart() {
        Set<Book> books = AdminUser.getShoppingcart().getBooks();
        Optional<Book> optionalBook = bookSet.stream().findFirst();
        assert optionalBook.isPresent();
        Assertions.assertThat(books.contains(optionalBook.get()));
    }
}