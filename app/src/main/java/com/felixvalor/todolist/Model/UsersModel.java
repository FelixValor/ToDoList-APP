package com.felixvalor.todolist.Model;

public class UsersModel {

    private int idUser;
    private String email, password, username;


    public UsersModel(int idUser, String email, String password, String username) {
        this.idUser = idUser;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public UsersModel(){

    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
