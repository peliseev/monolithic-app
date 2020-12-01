package ru.skillbox.monolithicapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryResponse {
    String currierName;
    EOrderStatus orderStatus;
    String orderStatusText;

    public DeliveryResponse(EOrderStatus orderStatus, String orderStatusText) {
        this.orderStatus = orderStatus;
        this.orderStatusText = orderStatusText;
    }
}
