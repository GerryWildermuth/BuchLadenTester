package com.Tester.BuchLadenTester.Model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Shoppingcart {
    public Shoppingcart() {
    }
    public Shoppingcart(Set<Book> books) {
        this.books=books;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shoppingcart_Id;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE
            })
    @JoinTable(name = "cart_book", joinColumns = @JoinColumn(name = "shoppingcart_Id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books = new HashSet<>(0);

    public int getShoppingcart_Id() {
        return shoppingcart_Id;
    }

    public void setShoppingcart_Id(int shoppingcart_Id) {
        this.shoppingcart_Id = shoppingcart_Id;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
