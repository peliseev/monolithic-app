package ru.skillbox.monolithicapp.model;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryOrderView {
    private int id;
    private EOrderStatus status;
    private String statusText;
    private String customerFullName;
    private String customerAddress;
    private String courierFullName;
    private List<ItemView> items;
}
