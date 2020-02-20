package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImpUnitTest {

    @Mock
    UserRepository userRepository;

    @Autowired//Not working with @InjectMocks because it is a Interface
    UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImp(userRepository);
    }

    @Test
    void saveUser() {
        User testUser = new User("test@test.de","12345","Tester","USER");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        User persistTestUser = userService.saveUser(testUser);
        assertThat(persistTestUser.getName()).isSameAs(testUser.getName());
        assertThat(persistTestUser.getPassword()).isNotEqualTo("12345");
    }


    @Test
    void isUserWithEmailAlreadyPresent() {
    }

    @Test
    void findByUsername() {
        User testUser = new User("test@test.de","12345","Tester","USER");
        when(userRepository.findByName(testUser.getName())).thenReturn(testUser);
        User foundUser = userService.findByUsername(testUser.getName());
        assertThat(foundUser.getName()).isEqualTo(testUser.getName());
    }
}