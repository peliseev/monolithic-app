package ru.skillbox.monolithicapp.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.User;
import ru.skillbox.monolithicapp.entity.Order;
import ru.skillbox.monolithicapp.exception.OrderNotFoundException;
import ru.skillbox.monolithicapp.exception.OrderСannotBeDeliveredException;
import ru.skillbox.monolithicapp.model.DeliveryOrderView;
import ru.skillbox.monolithicapp.model.DeliveryResponse;
import ru.skillbox.monolithicapp.model.EOrderStatus;
import ru.skillbox.monolithicapp.repository.OrderRepository;
import ru.skillbox.monolithicapp.util.Convertor;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DeliveryService {

    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    public DeliveryService(OrderRepository orderRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }

    public List<DeliveryOrderView> findOrdersForDelivery() {
        User courier = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Order> ordersForDelivery =
                orderRepository.findByStatusAndCourierId(EOrderStatus.ORDER_PAID, EOrderStatus.ORDER_COMING, courier.getId());
        return Convertor.orderToDeliveryOrder(ordersForDelivery);
    }

    public DeliveryResponse carryOrder(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        User courier = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (order.getStatus() != EOrderStatus.ORDER_PAID) {
            throw new OrderСannotBeDeliveredException();
        }

        order.setCourier(courier);
        order.setStatus(EOrderStatus.ORDER_COMING);
        orderRepository.save(order);

        notificationService.sendNotification(order);

        return new DeliveryResponse(courier.getFirstName() + " " + courier.getLastName(),
                order.getStatus(),
                order.getStatus().getHumanReadableText());
    }

    public DeliveryResponse deliver(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (order.getStatus() != EOrderStatus.ORDER_COMING) {
            throw new OrderСannotBeDeliveredException();
        }

        order.setStatus(EOrderStatus.ORDER_DELIVERED);

        notificationService.sendNotification(order);

        return new DeliveryResponse(order.getStatus(), order.getStatus().getHumanReadableText());
    }
}
