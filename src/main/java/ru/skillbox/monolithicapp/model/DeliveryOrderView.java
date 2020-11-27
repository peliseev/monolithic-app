package ru.skillbox.monolithicapp.model;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryOrderView {
    int id;
    EOrderStatus status;
    String customerFullName;
    String customerAddress;
    String courierFullName;
    String statusText;
    List<ItemView> items;
}
