package com.units.it.entities;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Account {
    @Id
    private String username;
    private String name;
    private String email;
    private String password;
    private String salt;

    @Index
    private String role;
    private String logo;

    public Account() {
    }

    public Account(Account account) {
        this.username = account.username;
        this.password = account.password;
        this.salt = "";
        this.name = account.name;
        this.email = account.email;
        this.role = account.role;
        this.logo = account.logo;
    }

    public Account(String username, String password, String name, String email, String role, String logo) {
        this.username = username;
        this.password = password;
        this.salt = "";
        this.name = name;
        this.email = email;
        this.role = role;
        this.logo = logo;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
