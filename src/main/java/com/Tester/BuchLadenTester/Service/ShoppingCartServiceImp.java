package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Model.Shoppingcart;
import com.Tester.BuchLadenTester.Model.User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ShoppingCartServiceImp implements ShoppingCartService {


    public ShoppingCartServiceImp(){

    }


    @Override
    public double totalPrice(Shoppingcart shoppingcart) {
        double totalPrice = 0;
        for(Book book: shoppingcart.getCartBooks())
        {
            totalPrice += book.getPrice();
        }
        return totalPrice;
    }

    @Override
    public boolean isBookAlreadyPresentInShoppingCart(Book book, User user) {
        Set<Book> books = user.getShoppingcart().getCartBooks();
        return books.contains(book);
    }
}
