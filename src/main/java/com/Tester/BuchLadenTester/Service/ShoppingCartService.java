package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Model.Shoppingcart;

public interface ShoppingCartService {
    boolean isBookAlreadyPresentinShoppingCart(Book book, User user);
    double totalPrice(Shoppingcart shoppingcart);
}
