package com.Tester.BuchLadenTester.Repository;

import com.Tester.BuchLadenTester.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByName(String name);
    @Override
    void delete(Book book);


}
