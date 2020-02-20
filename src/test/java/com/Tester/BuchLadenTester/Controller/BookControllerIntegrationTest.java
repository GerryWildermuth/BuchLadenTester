package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.BuchLadenTesterApplication;
import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Service.UserServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes= {BuchLadenTesterApplication.class, UserServiceImp.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableAutoConfiguration
@ActiveProfiles("prod")
@TestPropertySource(locations = {"classpath:application.properties","classpath:application-prod.properties","classpath:application-dev.properties"})
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private ShoppingcartRepository shoppingcartRepository;

    @Mock
    private UserServiceImp UserService;

    @Test
    void BooksOverview()throws Exception
    {
        List<Book> BookList = new ArrayList<Book>();
        mvc.perform(get("/books")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("books"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    void createBook() throws Exception {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTime().getTime());
        mvc.perform(post("/books/newBook")
                .param("name", "Horizon")
                .param("publishedDate", date.toString())
                .param("price", "50.0")
                .param("bookAuthors", "unknown unknown")
                .param("bookCover", " "))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "user", authorities = {"USER" })
    void createBookUserAccess() throws Exception {
        mvc.perform(post("/books/newBook"))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    void deleteBook()  throws Exception {
        mvc.perform(post("/books/deleteBook")
                .param("bookId", "2"))
                .andExpect(status().isOk());
        //.andExpect(model().attribute("successMessage","Book with bookId"+2+" got removed!"));
    }
}
