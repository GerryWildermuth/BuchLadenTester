package com.Tester.BuchLadenTester.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Book {
    public Book() {
    }

    public Book(Set<Author> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public Book(@NotNull String name, Set<Author> bookAuthors) {
        this.name = name;
        this.bookAuthors = bookAuthors;
    }

    public Book(String name, Date publishedDate,Author author, String bookCover, Double price) {
        this.name = name;
        this.publishedDate = publishedDate;
        this.bookCover = bookCover;
        this.price = price;
        bookAuthors.add(author);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int book_id;
    @NotNull
    private String name;
    private Date publishedDate;
    private String bookCover;//(Base64String)
    private Double price;

    @ManyToMany(mappedBy = "authorBooks",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private Set<Author> bookAuthors = new HashSet<>(0);

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int id) {
        this.book_id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public Set<Author> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(Set<Author> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }
}