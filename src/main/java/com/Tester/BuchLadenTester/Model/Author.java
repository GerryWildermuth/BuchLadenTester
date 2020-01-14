package com.Tester.BuchLadenTester.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
public class Author {
    public Author() {
    }
    public Author(String firstName,String lastName)
    {
        this.FirstName=firstName;
        this.LastName=lastName;
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
    private String Name = FirstName+""+LastName;
    @Column(name="birthDate")
    private Date BirthDate;
    //@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@JoinTable(name = "book_authro", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    //private Set<Book> books;


    public int getAuthor_id() {
        return author_id;
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
}
