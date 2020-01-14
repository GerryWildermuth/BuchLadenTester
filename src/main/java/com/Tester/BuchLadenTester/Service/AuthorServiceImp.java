package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Model.Author;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorServiceImp implements AuthorService {
    final
    AuthorRepository authorRepository;
    final
    BookRepository bookRepository;

    public AuthorServiceImp(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean isAuthorAlreadyPresent(Author author) {
        //Optional<Author> existingAuthor = authorRepository.findByName(author.getFirstName()+" "+ author.getLastName());
        Optional<Author> existingAuthor = authorRepository.findById(author.getAuthor_id());
        if(existingAuthor != null){
            return true;
        }
        return false;
    }
}
