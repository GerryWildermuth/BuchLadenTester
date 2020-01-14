package com.Tester.BuchLadenTester.Model;

//import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "name")
    private String name;

    @NotNull(message="Email is required")
    @Email(message = "Email is invalid")
    @Column(name = "email")
    private String email;

    @NotNull(message="Password is required")
    @Length(min=5, message="Password should be at least 5 characters")
    @Column(name = "password",length = 255)
    private String password;

    @Transient
    private String passwordConfirm;


    @Column(name = "status")
    private String status;

    @Column(name = "active")
    private int active;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles= new HashSet<>(0);

    @OneToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "user_book", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books = new HashSet<>(0);

    @OneToOne(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private Shoppingcart shoppingcart = new Shoppingcart();;


    public User() {

    }
    public User(String Email,String Password,String Name, String Role){
        this.email=Email;
        this.roles.add(new Role(Role,Role));
        this.name=Name;
        this.password=Password;
    }
    public User(String Email,String Password,String Name, Set<Role> Roles){
        this.email=Email;
        this.roles=Roles;
        this.name=Name;
        this.password=Password;
    }

    public User(User user) {
        this.active = user.getActive();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.name = user.getName();
        this.password = user.getPassword();
        shoppingcart = new Shoppingcart();
    }

    public User(String Email, String Password, String Name, Role role) {
        this.email=Email;
        this.roles.add(role);
        this.name=Name;
        this.password=Password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public Shoppingcart getShoppingcart() {
        return shoppingcart;
    }

    public void setShoppingcart(Shoppingcart shoppingcarts) {
        this.shoppingcart = shoppingcarts;
    }
}
