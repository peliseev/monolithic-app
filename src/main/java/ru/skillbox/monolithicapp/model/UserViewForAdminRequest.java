package ru.skillbox.monolithicapp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserViewForAdminRequest extends UserView {
    private EUserRole role;
}
