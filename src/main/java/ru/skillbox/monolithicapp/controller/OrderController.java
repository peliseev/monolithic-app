package ru.skillbox.monolithicapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.monolithicapp.entity.Item;
import ru.skillbox.monolithicapp.model.ItemView;
import ru.skillbox.monolithicapp.repository.ItemRepository;
import ru.skillbox.monolithicapp.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController extends BaseController {

    private final ItemRepository itemRepository;
    private final OrderService orderService;

    public OrderController(ItemRepository itemRepository, OrderService orderService) {
        this.itemRepository = itemRepository;
        this.orderService = orderService;
    }

    @GetMapping("items")
    public ResponseEntity<?> showItems() {
        List<Item> items = itemRepository.findAll();

        if (CollectionUtils.isEmpty(items)) {
            return ResponseEntity.notFound().build();
        }

        List<ItemView> responseList = items.stream()
                .map(item -> new ItemView(
                        item.getId(),
                        item.getName(),
                        item.getPrice().intValue(),
                        item.getQuantity()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @PostMapping("order")
    public ResponseEntity<?> createOrder(@RequestBody List<ItemView> items) {
        return orderService.createOrder(items) ?
                ResponseEntity.ok("Order created") :
                ResponseEntity.ok("Something got wrong");
    }

}
