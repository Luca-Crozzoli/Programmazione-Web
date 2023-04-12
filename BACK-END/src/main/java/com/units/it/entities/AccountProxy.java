package com.units.it.entities;

public class AccountProxy {

    private String username;
    private String name;
    private String email;
    private String logo;

    public AccountProxy() {
    }

    public AccountProxy(Account account) {
        this.username = account.getUsername();
        this.name = account.getName();
        this.email = account.getEmail();
        this.logo = account.getLogo();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
