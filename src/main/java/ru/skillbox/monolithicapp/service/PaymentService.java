package ru.skillbox.monolithicapp.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.User;
import ru.skillbox.monolithicapp.entity.Order;
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
    private final NotificationService notificationService;

    public PaymentService(OrderRepository orderRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }

    public CustomerOrderView pay(CustomerOrderView customerOrderView) {
        Order order = orderRepository.findById(customerOrderView.getId())
                .orElseThrow(OrderNotFoundException::new);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!validatePayment(order, user)) {
            throw new PaymentFailException();
        }

        order.setStatus(EOrderStatus.ORDER_PAID);
        orderRepository.save(order);

        notificationService.sendNotification(order);

        customerOrderView.setStatus(order.getStatus());
        customerOrderView.setStatusText(order.getStatus().getHumanReadableText());

        return customerOrderView;
    }

    private boolean validatePayment(Order order, User user) {

        // pay method will be success 80% of time
        boolean success = Math.random() > 0.2;

        return order.getCustomerId() == user.getId()
                && order.getStatus() == EOrderStatus.ORDER_CREATED
                && success;
    }
}
