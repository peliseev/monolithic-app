package ru.skillbox.monolithicapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skillbox.monolithicapp.entity.Order;
import ru.skillbox.monolithicapp.exception.InvalidStatusException;

@Service
public class NotificationService {

    @Value("${app.template.order-paid}")
    private String orderPaidTemplate;
    @Value("${app.template.order-coming}")
    private String orderComingTemplate;
    @Value("${app.template.order-delivered}")
    private String orderDeliveredTemplate;

    public void sendNotification(Order order) {
        String clientEmail = order.getCustomer().getEmail();

        switch (order.getStatus()) {
            case ORDER_PAID:
                System.out.println(String.format(orderPaidTemplate, clientEmail, order.getId()));
                break;
            case ORDER_COMING:
                System.out.println(String.format(orderComingTemplate, clientEmail,
                        order.getId(),
                        order.getCourier().getFirstName() + " " + order.getCourier().getLastName()));
                break;
            case ORDER_DELIVERED:
                System.out.println(String.format(orderDeliveredTemplate, clientEmail));
                break;
            default:
                throw new InvalidStatusException();
        }
    }

}
