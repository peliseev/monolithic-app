package ru.skillbox.monolithicapp.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.Customer;
import ru.skillbox.monolithicapp.entity.Item;
import ru.skillbox.monolithicapp.entity.Order;
import ru.skillbox.monolithicapp.entity.OrderItem;
import ru.skillbox.monolithicapp.exception.NotEnoughItemsException;
import ru.skillbox.monolithicapp.model.CustomerOrderView;
import ru.skillbox.monolithicapp.model.ItemView;
import ru.skillbox.monolithicapp.repository.ItemRepository;
import ru.skillbox.monolithicapp.repository.OrderRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
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

    public void createOrder(List<ItemView> items) {
        Map<Integer, ItemView> itemViewMap = items.stream()
                .collect(Collectors.toMap(ItemView::getId, Function.identity()));

        List<Item> dbItems = itemRepository.findAllById(itemViewMap.keySet());

        List<OrderItem> orderItems = new ArrayList<>();
        Order order = new Order();
        BigDecimal totalPrice = new BigDecimal(0);

        for (Item dbItem : dbItems) {
            int id = dbItem.getId();
            ItemView itemView = itemViewMap.get(id);

            int itemsInCart = itemView.getQuantity();

            if (itemsInCart > dbItem.getQuantity()) {
                throw new NotEnoughItemsException("Not enough items");
            }

            dbItem.setQuantity(dbItem.getQuantity() - itemsInCart);
            orderItems.add(new OrderItem(order, dbItem, itemsInCart));

            totalPrice = totalPrice.add(dbItem.getPrice().multiply(BigDecimal.valueOf(itemsInCart)));
        }

        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        order.setItems(orderItems);
        order.setCustomer(customer);
        order.setTotalPrice(totalPrice);
        order.setStatus(ORDER_CREATED);

        itemRepository.saveAll(dbItems);
        orderRepository.save(order);

    }

    public List<CustomerOrderView> getOrders() {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Order> orders = orderRepository.getByCustomerId(customer.getId());

        return orders.stream().map(order -> {
            CustomerOrderView customerOrderView = new CustomerOrderView();
            customerOrderView.setId(order.getId());
            customerOrderView.setStatus(order.getStatus());
            List<OrderItem> orderItems = order.getItems();
            List<ItemView> itemViews = orderItems.stream().map(OrderService::convertOrderItemToItemView)
                    .collect(Collectors.toList());
            customerOrderView.setItems(itemViews);
            return customerOrderView;
        }).collect(Collectors.toList());

    }

    private static ItemView convertOrderItemToItemView(OrderItem orderItem) {
        return new ItemView(orderItem.getItem().getId(),
                orderItem.getItem().getName(),
                orderItem.getItem().getPrice().intValue(),
                orderItem.getCount());
    }
}
