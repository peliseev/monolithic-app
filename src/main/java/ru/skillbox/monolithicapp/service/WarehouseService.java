package ru.skillbox.monolithicapp.service;

import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.Item;
import ru.skillbox.monolithicapp.exception.ItemNoFoundException;
import ru.skillbox.monolithicapp.model.ItemView;
import ru.skillbox.monolithicapp.repository.ItemRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WarehouseService {

    private final ItemRepository itemRepository;

    public WarehouseService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public int addItem(ItemView itemView) {
        Item item = new Item(
                itemView.getName(),
                itemView.getPrice(),
                itemView.getQuantity());
        Item savedItem = itemRepository.save(item);
        return savedItem.getId();
    }

    public void updateItems(List<ItemView> items) {
        List<Item> itemsToSave = items.stream().filter(item -> item.getId() == 0)
                .map(item -> new Item(
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity()))
                .collect(Collectors.toList());

        List<Item> itemsToUpdate = items.stream()
                .filter(item -> item.getId() != 0)
                .map(item -> {
                    Item itemToSave = itemRepository.findById(item.getId()).orElseThrow(ItemNoFoundException::new);
                    itemToSave.setName(item.getName());
                    itemToSave.setPrice(item.getPrice());
                    itemToSave.setQuantity(item.getQuantity());
                    return itemToSave;
                }).collect(Collectors.toList());

        itemsToSave.addAll(itemsToUpdate);
        itemRepository.saveAll(itemsToSave);
    }

    public void deleteItem(int id) {
        itemRepository.deleteById(id);
    }
}
