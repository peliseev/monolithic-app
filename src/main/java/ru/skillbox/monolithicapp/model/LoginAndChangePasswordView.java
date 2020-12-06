package ru.skillbox.monolithicapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginAndChangePasswordView {
    private String login;
    private String oldPassword;
    private String newPassword;
}
