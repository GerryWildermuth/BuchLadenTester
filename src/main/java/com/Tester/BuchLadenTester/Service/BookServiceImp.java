package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Model.Book;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImp implements BookService {
    final
    AuthorRepository authorRepository;
    final
    BookRepository bookRepository;

    public BookServiceImp(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public boolean isBookAlreadyPresent(Book book) {
        boolean isBookAlreadyExists = false;
        Optional<Book> existingBook = bookRepository.findByName(book.getName());
        if(existingBook.isPresent()){
            isBookAlreadyExists = true;
        }
        return isBookAlreadyExists;
    }
}
