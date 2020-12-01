package ru.skillbox.monolithicapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.monolithicapp.exception.NoItemsException;
import ru.skillbox.monolithicapp.model.CustomerOrderView;
import ru.skillbox.monolithicapp.model.ItemView;
import ru.skillbox.monolithicapp.service.OrderService;
import ru.skillbox.monolithicapp.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @PostMapping
    public void createOrder(@RequestBody List<ItemView> items) {
        if (items.isEmpty()) { throw new NoItemsException("Empty list of items"); }
        orderService.createOrder(items);
    }

    @GetMapping("all")
    public ResponseEntity<List<CustomerOrderView>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @PostMapping("pay")
    public ResponseEntity<CustomerOrderView> pay(@RequestBody CustomerOrderView customerOrderView) {
        return ResponseEntity.ok(paymentService.pay(customerOrderView));
    }

}
