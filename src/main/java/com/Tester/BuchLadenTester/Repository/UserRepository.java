package com.Tester.BuchLadenTester.Repository;

import com.Tester.BuchLadenTester.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String username);
    User findByEmail(String email);
    @Override
    void delete(User user);
}
