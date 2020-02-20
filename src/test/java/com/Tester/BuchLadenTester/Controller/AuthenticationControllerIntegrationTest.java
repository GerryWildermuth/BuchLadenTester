package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.BuchLadenTesterApplication;
import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Service.UserServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(
            classes= {BuchLadenTesterApplication.class, UserServiceImp.class},
            webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableAutoConfiguration
@ActiveProfiles("prod")
@TestPropertySource(locations = {"classpath:application.properties","classpath:application-prod.properties","classpath:application-dev.properties"})
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthenticationControllerIntegrationTest
{

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ShoppingcartRepository shoppingcartRepository;

    @Autowired
    private UserServiceImp UserService;

/*
    @Test
    //after correct login you will be redirected to /books
    void loginWithValidUser() throws Exception {
        mvc.perform(post("/login")
                .param("email", "admin@web.de")
                .param("password", "12345"))
                .andExpect(status().is(302));
    }*/

    /*@Test
    void loginPostWithInvalidUser() throws Exception {
        mvc.perform(post("/login")
                .param("email", "somebody@web.de")
                .param("password", "notAPassword"))
                .andExpect(status().is(302));
    }*/
/*

    @Test
    void registerUser() throws Exception {
        mvc.perform(post("/register")
                .param("email", "userzwei@web.de")
                .param("password", "12345")
                .param("name", "User")
                .param("userRoles", "USER"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("successMessage","User is registered successfully! with name: User"));
    }
*/
/*
    @Test
    @WithMockUser(username = "admin@web.de", authorities = { "ADMIN", "USER" })
    void home() throws Exception {
        mvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("userBooks"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("books"));
    }
    @Test
    @WithMockUser(username = "admin@web.de", authorities = { "ADMIN", "USER" })
    void admin() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));
    }*/
}
