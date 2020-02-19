package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.BuchLadenTesterApplication;
import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Service.UserServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

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
    public class AuthenticationControllerIntegrationTest {

        @Test
        void contextLoads() {
        }

        @Autowired
        private MockMvc mvc;

        @Autowired
        private AuthorRepository authorRepository;

        @Autowired
        private ShoppingcartRepository shoppingcartRepository;

        @Autowired
        private UserServiceImp UserService;


        @Test
        //after correct login you will be redirected to /books
    void loginPost() throws Exception {
        mvc.perform(post("/login")
                .param("email", "admin@web.de")
                .param("password", "12345"))
                .andExpect(status().is(302));
    }

    @Test
    void registerUserPost() throws Exception {
        mvc.perform(post("/register")
                .param("email", "userzwei@web.de")
                .param("password", "12345")
                .param("name", "User")
                .param("userRoles", "USER"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("successMessage","User is registered successfully! with name: User"));
    }
}
