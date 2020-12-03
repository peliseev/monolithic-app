package ru.skillbox.monolithicapp.model;

import lombok.Data;

@Data
public class UserView {
    private String login;
    private String password;
    private String passwordConfirm;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
}
