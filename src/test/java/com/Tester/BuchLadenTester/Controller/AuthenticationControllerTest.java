package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.BuchLadenTesterApplication;
import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Repository.*;
import com.Tester.BuchLadenTester.Service.*;
import com.Tester.BuchLadenTester.Helper.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BuchLadenTesterApplication.class, UserServiceImp.class,
        SecurityServiceImpl.class, UserDetailsServiceImpl.class,
        AuthorServiceImp.class, ShoppingCartServiceImp.class,BookServiceImp.class})
@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    AuthorRepository authorRepository;

    @MockBean
    ShoppingcartRepository shoppingcartRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    BookRepository bookRepository;

    @MockBean
    RoleRepository roleRepository;

    private List<Book> BookList;
    private List<String> RoleList;

    @PostConstruct
    void beforeSetUp()
    {
        BookList = new ArrayList<>();
        RoleList = new ArrayList<String>();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void adminHome() {
    }

    @Test
    void register() throws Exception {
        RoleList.add("ADMIN");
        RoleList.add("USER");
        mvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(MockMvcResultMatchers.model().attribute("userRoles", RoleList));
    }

    @Test
    void registerUserPost() throws Exception {
        mvc.perform(post("/register")
                .param("email", "user@web.de")
                .param("password", "12345")
                .param("name", "User")
                .param("userRoles", "USER"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(username = "admin@web.de",password = "12345")
    void loginPost() throws Exception {
        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void loginWithMock() throws Exception {
       /* RegisterClass registerClass = new RegisterClass("test@test.de","12345");
        String json = mapper.writeValueAsString(registerClass);*/
        mvc.perform(formLogin("/login").user("email","admin@web.de")
                .password("password","12345"))
                .andExpect(status().isOk());
    }

    @Test
    void loginPostWithInvalidUser() throws Exception {
        RegisterClass registerClass = new RegisterClass("NOUSERWITHTHISEMAIL@web.de","123456");
        String json = mapper.writeValueAsString(registerClass);
        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(302));
    }

    @Test
    void home() throws Exception {
        mvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("userBooks"))
                .andExpect(MockMvcResultMatchers.model().attribute("books", BookList));
    }

    @Test
    void initBinder() {
    }
}