package ru.skillbox.monolithicapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemView {
    private int id;
    private String name;
    private int price;
    private int quantity;
}
