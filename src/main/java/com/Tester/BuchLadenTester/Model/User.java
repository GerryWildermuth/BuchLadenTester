package com.Tester.BuchLadenTester.Model;

//import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
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

    //Not used in any practical way
    @Column(name = "status")
    private String status;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> userRoles = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,})
    @JoinTable(
            name = "user_book",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> userBooks = new HashSet<>(0);

    @OneToOne(fetch = FetchType.EAGER, optional = false,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.REMOVE
            })
    @JoinColumn(name = "userId", nullable = false)
    private Shoppingcart shoppingcart;

    public User() {
        this.shoppingcart = new Shoppingcart();
    }
    public User(String Email,String Password,String Name, String Role){
        this.email=Email;
        this.userRoles.add(new Role(Role,Role));
        this.name=Name;
        this.password=Password;
        this.shoppingcart = new Shoppingcart();
    }
    public User(String Email,String Password,String Name, Set<Role> Roles){
        this.email=Email;
        this.userRoles =Roles;
        this.name=Name;
        this.password=Password;
        this.shoppingcart = new Shoppingcart();
    }

    public User(User user) {
        this.email = user.getEmail();
        this.userRoles = user.getUserRoles();
        this.name = user.getName();
        this.password = user.getPassword();
        this.shoppingcart = new Shoppingcart();
        this.userBooks = user.getUserBooks();
        this.status=user.getStatus();
        this.passwordConfirm=user.getPasswordConfirm();
    }

    public User(String Email, String Password, String Name, Role role) {
        this.email=Email;
        this.userRoles.add(role);
        this.name=Name;
        this.password=Password;
        this.shoppingcart = new Shoppingcart();
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

    public Set<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<Role> roles) {
        this.userRoles = roles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Book> getUserBooks() {
        return userBooks;
    }

    public void setUserBooks(Set<Book> books) {
        this.userBooks = books;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    public void addRoleUser(Role userRole) {
        this.userRoles.add(userRole);
        userRole.getRoleUsers().add(this);
    }

    public void removeRoleUser(Role userRole) {
        this.userRoles.remove(userRole);
        userRole.getRoleUsers().remove(this);
    }

    public Shoppingcart getShoppingcart() {
        return shoppingcart;
    }

    public void setShoppingcart(Shoppingcart shoppingcarts) {
        this.shoppingcart = shoppingcarts;
    }
}
