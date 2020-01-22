package com.Tester.BuchLadenTester.Model;

import org.springframework.data.relational.core.sql.In;

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
        this.FirstName=firstName;
        this.LastName=lastName;
        this.BirthDate=birthDate;
        this.Name = firstName + " " + lastName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="author_id")
    private int author_id;

    @NotNull
    @Column(name="firstName",unique = true)
    private String FirstName;

    @NotNull
    @Column(name="lastName",unique = true)
    private String LastName;

    @Transient
    private String Name;

    @Column(name="birthDate")
    private Date BirthDate;

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
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public Date getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(Date birthDate) {
        BirthDate = birthDate;
    }

    public String getName() {return Name;}

    public void setName(String name) {
        Name = name;
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
