package ru.skillbox.monolithicapp.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.Customer;
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

    public PaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CustomerOrderView pay(CustomerOrderView customerOrderView) {
        Order order = orderRepository.findById(customerOrderView.getId())
                .orElseThrow(OrderNotFoundException::new);
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!validatePayment(order, customer)) {
            throw new PaymentFailException();
        }

        order.setStatus(EOrderStatus.ORDER_PAID);
        orderRepository.save(order);

        customerOrderView.setStatus(order.getStatus());
        customerOrderView.setStatusText(order.getStatus().getHumanReadable());

        return customerOrderView;
    }

    private boolean validatePayment(Order order, Customer customer) {

        // pay method will be success 80% of time
        boolean success = Math.random() > 0.2;

        return order.getCustomerId() != customer.getId()
                && order.getStatus() != EOrderStatus.ORDER_CREATED
                && success;
    }
}
