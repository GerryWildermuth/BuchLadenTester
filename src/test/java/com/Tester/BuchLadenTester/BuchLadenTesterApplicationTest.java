package com.Tester.BuchLadenTester;

import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Service.UserServiceImp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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
        classes= {BuchLadenTesterApplication.class,UserServiceImp.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableAutoConfiguration
@ActiveProfiles("prod")
@TestPropertySource(locations = {"classpath:application.properties","classpath:application-prod.properties","classpath:application-dev.properties"})
class BuchLadenTesterApplicationTest {

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


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


}