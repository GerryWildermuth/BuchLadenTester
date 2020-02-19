package com.Tester.BuchLadenTester.Repository;

import com.Tester.BuchLadenTester.BuchLadenTesterApplication;
import com.Tester.BuchLadenTester.Controller.BookController;
import com.Tester.BuchLadenTester.Model.Author;
import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Model.CustomUserDetails;
import com.Tester.BuchLadenTester.Service.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BuchLadenTesterApplication.class,UserServiceImp.class})
@TestPropertySource(locations = {"classpath:application.properties","classpath:application-prod.properties","classpath:application-dev.properties"})
//@DataJpaTest supports rollback after running every test case
@DataJpaTest()
class BookRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Date date;

    @BeforeEach
    void setUp() {
        Calendar calendar = Calendar.getInstance();
        date = new Date(calendar.getTime().getTime());
    }

    @Test
    void bookOverview(){
        List<Book> all = bookRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(5);
        Optional<Book> book2 = all.stream().filter(book -> book.getName().equals("ABook")).findFirst();
        Optional<Book> book3 = all.stream().filter(book -> book.getName().equals("Horizon")).findFirst();
        if(book2.isPresent()&&book3.isPresent()) {
            Assertions.assertThat(book2.get().getName()).isEqualTo("ABook");
            Assertions.assertThat(book3.get().getName()).isEqualTo("Horizon");
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void whenFindByName_thenReturnBook() {
        Author author1 = new Author("other","unknown",date);
        Book Book1 = new Book("myNewBook",date,author1,"",5.0);
        author1.getAuthorBooks().add(Book1);
        testEntityManager.persistAndFlush(author1);
        Optional<Book> foundBook = bookRepository.findByName("myNewBook");
        foundBook.ifPresent(book -> assertThat(book.getName()).isEqualTo("myNewBook"));
        Optional<Author> foundAuthor = authorRepository.findByName("other unknown");
        foundAuthor.ifPresent(author -> assertThat(author.getName()).isEqualTo("other unknown"));
    }
}