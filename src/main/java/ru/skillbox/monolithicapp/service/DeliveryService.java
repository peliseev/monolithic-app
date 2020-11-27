package ru.skillbox.monolithicapp.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.Customer;
import ru.skillbox.monolithicapp.entity.Order;
import ru.skillbox.monolithicapp.exception.OrderNotFoundException;
import ru.skillbox.monolithicapp.exception.OrderСannotBeDeliveredException;
import ru.skillbox.monolithicapp.model.DeliveryOrderView;
import ru.skillbox.monolithicapp.model.EOrderStatus;
import ru.skillbox.monolithicapp.repository.OrderRepository;
import ru.skillbox.monolithicapp.util.Convertor;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DeliveryService {

    private final OrderRepository orderRepository;

    public DeliveryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<DeliveryOrderView> findOrdersForDelivery() {
        List<Order> ordersForDelivery =
                orderRepository.findByStatus(EOrderStatus.ORDER_PAID, EOrderStatus.ORDER_COMING);
        return Convertor.orderToDeliveryOrder(ordersForDelivery);
    }

    public DeliveryOrderView carryOrder(DeliveryOrderView deliveryOrderView) {
        Order order = orderRepository.findById(deliveryOrderView.getId())
                .orElseThrow(OrderNotFoundException::new);
        Customer courier = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (order.getStatus() != EOrderStatus.ORDER_PAID) {
            throw new OrderСannotBeDeliveredException();
        }

        deliveryOrderView.setCourierFullName(courier.getFirstName() + " " + courier.getLastName());
        order.setCourier(courier);

        return deliveryOrderView;
    }

    public void deliver(DeliveryOrderView deliveryOrderView) {
        Order order = orderRepository.findById(deliveryOrderView.getId())
                .orElseThrow(OrderNotFoundException::new);
        Customer courier = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (order.getStatus() != EOrderStatus.ORDER_COMING) {
            throw new OrderСannotBeDeliveredException();
        }

        order.setStatus(EOrderStatus.ORDER_DELIVERED);
    }
}
