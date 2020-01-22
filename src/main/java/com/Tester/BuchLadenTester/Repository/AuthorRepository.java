package com.Tester.BuchLadenTester.Repository;

import com.Tester.BuchLadenTester.Model.Author;
import com.Tester.BuchLadenTester.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
