package ru.skillbox.monolithicapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.monolithicapp.model.DeliveryOrderView;
import ru.skillbox.monolithicapp.service.DeliveryService;

@RestController
@RequestMapping("api/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("orders")
    public ResponseEntity getOrdersForDelivery() {
        return ResponseEntity.ok(deliveryService.findOrdersForDelivery());
    }

    @PostMapping("order/{id}/carry")
    public ResponseEntity carryOrder(DeliveryOrderView deliveryOrderView) {
        return ResponseEntity.ok(deliveryService.carryOrder(deliveryOrderView));
    }

    @PostMapping("deliver")
    public void deliver(DeliveryOrderView deliveryOrderView) {
        deliveryService.deliver(deliveryOrderView);
    }

}
