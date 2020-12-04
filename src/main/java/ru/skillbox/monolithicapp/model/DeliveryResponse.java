package ru.skillbox.monolithicapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryResponse {
    private String currierName;
    private EOrderStatus orderStatus;
    private String orderStatusText;

    public DeliveryResponse(EOrderStatus orderStatus, String orderStatusText) {
        this.orderStatus = orderStatus;
        this.orderStatusText = orderStatusText;
    }
}
