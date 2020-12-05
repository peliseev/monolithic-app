package ru.skillbox.monolithicapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class LoginResponse {
    private boolean passwordMustBeChanged;
    private Set<String> roles;
}
