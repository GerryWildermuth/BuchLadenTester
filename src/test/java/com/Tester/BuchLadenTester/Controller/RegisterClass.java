package com.Tester.BuchLadenTester.Controller;

public class RegisterClass {


    private String Email;
    private String Password;
    private String name;
    private String userRoles;


    public RegisterClass(String email, String password, String name, String userRoles) {
        this.Email = email;
        this.Password = password;
        this.name = name;
        this.userRoles = userRoles;
    }

    public RegisterClass() {
    }

    public RegisterClass(String email, String password) {
        this.Email = email;
        this.Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(String userRoles) {
        this.userRoles = userRoles;
    }

}
