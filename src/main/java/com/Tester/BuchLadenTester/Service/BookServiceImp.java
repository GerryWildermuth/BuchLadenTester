package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Model.Book;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImp implements BookService {

    final
    BookRepository bookRepository;

    public BookServiceImp( BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public boolean isBookAlreadyPresent(Book book) {
        Optional<Book> existingBook = bookRepository.findById(book.getBook_id());
        return existingBook.isPresent();
    }
}
