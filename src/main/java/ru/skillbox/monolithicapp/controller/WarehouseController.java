package ru.skillbox.monolithicapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.monolithicapp.entity.Item;
import ru.skillbox.monolithicapp.model.ItemView;
import ru.skillbox.monolithicapp.repository.ItemRepository;
import ru.skillbox.monolithicapp.repository.OrderRepository;
import ru.skillbox.monolithicapp.service.WarehouseService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final ItemRepository itemRepository;

    public WarehouseController(WarehouseService warehouseService, OrderRepository orderRepository, ItemRepository itemRepository) {
        this.warehouseService = warehouseService;
        this.itemRepository = itemRepository;
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

    @PostMapping("item")
    public ResponseEntity<Integer> addItem(@RequestBody ItemView itemToAdd) {
        return ResponseEntity.ok(warehouseService.addItem(itemToAdd));
    }

    @PutMapping("items")
    public void updateItems(@RequestBody List<ItemView> itemView) {
        warehouseService.updateItems(itemView);
    }

    @DeleteMapping("item/{id}")
    public void deleteItem(@PathVariable int id) {
        warehouseService.deleteItem(id);
    }

}
