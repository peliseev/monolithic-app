package ru.skillbox.monolithicapp.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.Customer;
import ru.skillbox.monolithicapp.entity.Item;
import ru.skillbox.monolithicapp.entity.Order;
import ru.skillbox.monolithicapp.entity.OrderItem;
import ru.skillbox.monolithicapp.model.ItemView;
import ru.skillbox.monolithicapp.repository.ItemRepository;
import ru.skillbox.monolithicapp.repository.OrderRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    private static final String ORDER_CREATED = "ORDER_CREATED";

    public OrderService(ItemRepository itemRepository,
                        OrderRepository orderRepository) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    public boolean createOrder(List<ItemView> items) {
        Map<Integer, ItemView> itemViewMap = items.stream()
                .collect(Collectors.toMap(ItemView::getId, Function.identity()));

        List<Item> dbItems = itemRepository.findAllById(itemViewMap.keySet());
        List<OrderItem> orderItems = new ArrayList<>();
        Order order = new Order();

        for (Item dbItem : dbItems) {
            int id = dbItem.getId();
            ItemView itemView = itemViewMap.get(id);
            int itemsInCart = itemView.getQuantity();
            if (itemsInCart > dbItem.getQuantity()) {
                return false;
            }
            dbItem.setQuantity(dbItem.getQuantity() - itemView.getQuantity());
            orderItems.add(new OrderItem(order, dbItem, itemsInCart));
        }

        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        order.setItems(orderItems);
        order.setCustomer(customer);
        order.setStatus(ORDER_CREATED);

        itemRepository.saveAll(dbItems);
        orderRepository.save(order);

        return true;
    }
}
