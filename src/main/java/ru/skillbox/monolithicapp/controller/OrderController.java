package ru.skillbox.monolithicapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.monolithicapp.entity.Item;
import ru.skillbox.monolithicapp.exception.NoItemsException;
import ru.skillbox.monolithicapp.model.CustomerOrderView;
import ru.skillbox.monolithicapp.model.ItemView;
import ru.skillbox.monolithicapp.repository.ItemRepository;
import ru.skillbox.monolithicapp.service.OrderService;
import ru.skillbox.monolithicapp.service.PaymentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class OrderController {

    private final ItemRepository itemRepository;
    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(ItemRepository itemRepository, OrderService orderService, PaymentService paymentService) {
        this.itemRepository = itemRepository;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("items")
    public ResponseEntity<List<ItemView>> showItems() {
        List<Item> items = itemRepository.findAll();

        if (CollectionUtils.isEmpty(items)) {
            return ResponseEntity.notFound().build();
        }

        List<ItemView> responseList = items.stream()
                .map(item -> new ItemView(
                        item.getId(),
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @PostMapping("order")
    public void createOrder(@RequestBody List<ItemView> items) {
        if (items.isEmpty()) { throw new NoItemsException("Empty list of items"); }
        orderService.createOrder(items);
    }

    @GetMapping("orders")
    public ResponseEntity getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @PostMapping("pay")
    public ResponseEntity pay(@RequestBody CustomerOrderView customerOrderView) {
        return ResponseEntity.ok(paymentService.pay(customerOrderView));
    }

}
