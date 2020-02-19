package com.Tester.BuchLadenTester.Repository;

import com.Tester.BuchLadenTester.BuchLadenTesterApplication;
import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Service.UserServiceImp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BuchLadenTesterApplication.class,UserServiceImp.class})
@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void findByName() {
        User alex = new User("alex@test.de","12345","alex","USER");
        Mockito.when(userRepository.findByName(alex.getName())).thenReturn(alex);
        User found = userRepository.findByName(alex.getName());
        assertThat(found.getName()).isEqualTo(alex.getName());
    }

    @Test
    void findByEmail() {
        User alex = new User("alex@test.de","12345","alex","USER");
        Mockito.when(userRepository.findByEmail(alex.getEmail())).thenReturn(alex);
        User found = userRepository.findByEmail(alex.getEmail());
        assertThat(found.getEmail()).isEqualTo(alex.getEmail());
    }

    @Test
    void delete() {
    }
}