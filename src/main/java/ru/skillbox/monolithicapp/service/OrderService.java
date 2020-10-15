package ru.skillbox.monolithicapp.service;

import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.Item;
import ru.skillbox.monolithicapp.model.ItemView;
import ru.skillbox.monolithicapp.repository.ItemRepository;
import ru.skillbox.monolithicapp.repository.OrderRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private ItemRepository itemRepository;
    private OrderRepository orderRepository;

    public OrderService(ItemRepository itemRepository, OrderRepository orderRepository) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    public boolean createOrder(List<ItemView> items) {
        List<Item> dbItems = itemRepository.findAllById(items.stream()
                .map(ItemView::getId)
                .collect(Collectors.toList()));

        return true;
    }
}
