package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Model.Author;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorServiceImp implements AuthorService {
    final
    AuthorRepository authorRepository;

    public AuthorServiceImp(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void saveAuthor(Author author) {
        authorRepository.save(author);
    }

    @Override
    public Author findByName(String Name) {
        return null;
    }

    @Override
    public boolean isAuthorAlreadyPresent(Author author) {
        Optional<Author> existingAuthor = authorRepository.findById(author.getAuthor_id());
        return existingAuthor.isPresent();
    }

}
