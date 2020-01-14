package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.Model.Book;

public interface BookService {
    void saveBook(Book book);

    boolean isBookAlreadyPresent(Book book);
}
