package ru.skillbox.monolithicapp.controller;

import org.springframework.web.bind.annotation.*;
import ru.skillbox.monolithicapp.model.ItemView;
import ru.skillbox.monolithicapp.service.WarehouseService;

import java.util.List;

@RestController
@RequestMapping("api/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping("/item")
    public void addItem(@RequestBody ItemView itemToAdd) {
        warehouseService.addItem(itemToAdd);
    }

    @PutMapping("/items")
    public void updateItems(@RequestBody List<ItemView> itemView) {
        warehouseService.updateItems(itemView);
    }

    @DeleteMapping("/item/{id}")
    public void deleteItem(@PathVariable int id) {
        warehouseService.deleteItem(id);
    }

}
