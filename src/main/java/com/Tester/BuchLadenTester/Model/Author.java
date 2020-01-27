package com.Tester.BuchLadenTester.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Author {
    public Author() {
    }
    public Author(String firstName, String lastName, Date birthDate)
    {
        this.firstName =firstName;
        this.lastName =lastName;
        this.birthDate =birthDate;
        this.name = firstName + " " + lastName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="author_id")
    private int author_id;

    @NotNull
    @Column(name="firstName")
    private String firstName;

    @NotNull
    @Column(name="lastName")
    private String lastName;

    @NotNull
    @Column(name="name",unique = true)
    private String name;

    @Column(name="birthDate")
    private Date birthDate;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    Set<Book> authorBooks = new HashSet<>();


    public int getAuthor_id() {
        return author_id;
    }

    public String getIdAsString() {
        return Integer.valueOf(author_id).toString();
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getAuthorBooks() {
        return authorBooks;
    }

    public void setAuthorBooks(Set<Book> authorBooks) {
        this.authorBooks = authorBooks;
    }
    public void addBookAuthors(Book authorBooks) {
        this.authorBooks.add(authorBooks);
        authorBooks.getBookAuthors().add(this);
    }

    public void removeBookAuthors(Author authorBooks) {
        this.authorBooks.remove(authorBooks);
        authorBooks.getAuthorBooks().remove(this);
    }
}
