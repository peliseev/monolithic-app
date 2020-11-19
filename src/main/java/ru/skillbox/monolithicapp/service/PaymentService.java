package ru.skillbox.monolithicapp.service;

import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.Order;
import ru.skillbox.monolithicapp.exception.OrderAlreadyPaidException;
import ru.skillbox.monolithicapp.exception.OrderNotFoundException;
import ru.skillbox.monolithicapp.exception.PaymentFailException;
import ru.skillbox.monolithicapp.model.CustomerOrderView;
import ru.skillbox.monolithicapp.model.EOrderStatus;
import ru.skillbox.monolithicapp.repository.OrderRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class PaymentService {

    private final OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CustomerOrderView pay(CustomerOrderView customerOrderView) {
        Order order = orderRepository.findById(customerOrderView.getId())
                .orElseThrow(OrderNotFoundException::new);

        if (order.getStatus() != EOrderStatus.ORDER_CREATED) {
            throw new OrderAlreadyPaidException();
        }

        // pay method will be success 80% of time
        boolean success = Math.random() > 0.2;

        if (!success) {
            throw new PaymentFailException();
        }

        order.setStatus(EOrderStatus.ORDER_PAID);
        orderRepository.save(order);

        customerOrderView.setStatus(order.getStatus());
        customerOrderView.setStatusText(order.getStatus().getHumanReadable());

        return customerOrderView;
    }
}
