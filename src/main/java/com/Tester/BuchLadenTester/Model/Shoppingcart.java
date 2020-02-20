package com.Tester.BuchLadenTester.Model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Shoppingcart {
    public Shoppingcart() {
    }

    public Shoppingcart(Set<Book> books) {
        this.cartBooks = books;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shoppingcart_Id;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
    })
    @JoinTable(name = "cart_book", joinColumns = @JoinColumn(name = "shoppingcart_Id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> cartBooks = new HashSet<>(0);

    public int getShoppingcart_Id() {
        return shoppingcart_Id;
    }

    public void setShoppingcart_Id(int shoppingcart_Id) {
        this.shoppingcart_Id = shoppingcart_Id;
    }

    public Set<Book> getCartBooks() {
        return cartBooks;
    }

    public void setCartBooks(Set<Book> cartBooks) {
        this.cartBooks = cartBooks;
    }

    public void addBookCarts(Book cartBooks) {
        this.cartBooks.add(cartBooks);
        cartBooks.getBookCarts().add(this);
    }

    public void removeBookCarts(Shoppingcart cartBooks) {
        this.cartBooks.remove(cartBooks);
        cartBooks.getCartBooks().remove(this);
    }
}