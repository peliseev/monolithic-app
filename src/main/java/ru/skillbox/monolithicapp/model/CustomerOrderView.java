package ru.skillbox.monolithicapp.model;

import lombok.Data;

import java.util.List;

@Data
public class CustomerOrderView {
    int id;
    String status;
    List<ItemView> items;
}
