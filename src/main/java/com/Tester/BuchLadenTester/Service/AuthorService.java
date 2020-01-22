package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.Model.Author;

public interface AuthorService {

    boolean isAuthorAlreadyPresent(Author author);
    void saveAuthor(Author author);
    Author findByName(String Name);
}
