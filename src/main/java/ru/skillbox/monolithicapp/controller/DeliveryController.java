package ru.skillbox.monolithicapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.monolithicapp.model.DeliveryOrderView;
import ru.skillbox.monolithicapp.model.DeliveryResponse;
import ru.skillbox.monolithicapp.service.DeliveryService;

import java.util.List;

@RestController
@RequestMapping("api/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("orders")
    public ResponseEntity<List<DeliveryOrderView>> getOrdersForDelivery() {
        return ResponseEntity.ok(deliveryService.findOrdersForDelivery());
    }

    @PostMapping("order/{id}/carry")
    public ResponseEntity<DeliveryResponse> carryOrder(@PathVariable int id) {
        return ResponseEntity.ok(deliveryService.carryOrder(id));
    }

    @PostMapping("order/{id}/deliver")
    public ResponseEntity<DeliveryResponse> deliver(@PathVariable int id) {
        return ResponseEntity.ok(deliveryService.deliver(id));
    }

}
