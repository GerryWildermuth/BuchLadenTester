package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Model.Shoppingcart;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ShoppingCartImp implements ShoppingCartService {

    final
    AuthorRepository authorRepository;
    final
    BookRepository bookRepository;

    public ShoppingCartImp(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }


    @Override
    public double totalPrice(Shoppingcart shoppingcart) {
        double totalPrice = 0;
        for(Book book: shoppingcart.getBooks())
        {
            totalPrice += book.getPrice();
        }
        return totalPrice;
    }

    @Override
    public boolean isBookAlreadyPresentinShoppingCart(Book book, User user) {
        Set<Book> books = user.getShoppingcart().getBooks();
        return books.contains(book);
    }
}
