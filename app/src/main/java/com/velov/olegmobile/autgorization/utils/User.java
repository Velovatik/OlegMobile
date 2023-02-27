package com.velov.olegmobile.autgorization.utils;

public class User {
    private String login;
    private String password;

    private String name;

    //Overloaded constructors for different params
    public User(String name, String login, String password) {
        this.login = login;
        this.password = password;
        this.name = name;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    //Getters and Setters
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
}
