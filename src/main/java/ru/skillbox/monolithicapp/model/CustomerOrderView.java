package ru.skillbox.monolithicapp.model;

import lombok.Data;

import java.util.List;

@Data
public class CustomerOrderView {
    int id;
    EOrderStatus status;
    String statusText;
    int totalPrice;
    List<ItemView> items;
}
