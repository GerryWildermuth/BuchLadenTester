package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.BuchLadenTesterApplication;
import com.Tester.BuchLadenTester.Model.Author;
import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Model.Shoppingcart;
import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Service.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes= {BuchLadenTesterApplication.class, UserServiceImp.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableAutoConfiguration
@ActiveProfiles("prod")
@TestPropertySource(locations = {"classpath:application.properties","classpath:application-prod.properties","classpath:application-dev.properties"})
@ExtendWith(MockitoExtension.class)
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ShoppingcartRepository shoppingcartRepository;

    @BeforeEach
    void setUp() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
    }

    @Test
    @WithMockUser(username = "admin@web.de", authorities = { "ADMIN", "USER" })
    void addBookToUserCart()throws Exception {
        List<Book> bookList = bookRepository.findAll();
        Optional<Book> first = bookList.stream().findAny();
        if(first.isPresent()) {
            int book_id = first.get().getBook_id();

            mvc.perform(post("/shoppingcart")
                    .param("bookId", ""+book_id))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @WithMockUser(username = "admin@web.de", authorities = { "ADMIN", "USER" })
    void buyBooksInCart() throws Exception
    {
        mvc.perform(post("/shoppingcart")
                .param("bookId", "3"))
                .andExpect(status().isOk());


        mvc.perform(post("/shoppingcart/buyBooks"))
                .andExpect(status().isOk());
    }
}