package ru.skillbox.monolithicapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogInView {
    private String login;
    private String password;
}
