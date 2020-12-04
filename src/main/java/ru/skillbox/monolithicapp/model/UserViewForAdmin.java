package ru.skillbox.monolithicapp.model;

import lombok.Data;

import java.util.List;

@Data
public class UserViewForAdmin {
    private Integer id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private List<EUserRole> roles;
}
