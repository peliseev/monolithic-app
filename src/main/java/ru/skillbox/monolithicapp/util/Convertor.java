package ru.skillbox.monolithicapp.util;

import ru.skillbox.monolithicapp.entity.Order;
import ru.skillbox.monolithicapp.entity.OrderItem;
import ru.skillbox.monolithicapp.model.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Convertor {

    public static ItemView orderItemToItemView(OrderItem orderItem) {
        return new ItemView(orderItem.getItem().getId(),
                orderItem.getItem().getName(),
                orderItem.getItem().getPrice(),
                orderItem.getCount());
    }

    public static List<DeliveryOrderView> orderToDeliveryOrder(List<Order> orders) {
        return orders.stream()
                .map(order -> {
                    DeliveryOrderView deliveryOrderView = new DeliveryOrderView();
                    deliveryOrderView.setId(order.getId());
                    deliveryOrderView.setStatus(order.getStatus());
                    deliveryOrderView.setStatusText(order.getStatus().getHumanReadableText());
                    deliveryOrderView.setCustomerFullName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
                    deliveryOrderView.setCustomerAddress(order.getCustomer().getAddress());
                    if (order.getStatus() == EOrderStatus.ORDER_COMING) {
                        deliveryOrderView.setCourierFullName(
                                order.getCourier().getFirstName() + " " + order.getCourier().getLastName());
                    }
                    deliveryOrderView.setItems(order.getItems().stream()
                            .map(Convertor::orderItemToItemView)
                            .collect(Collectors.toList()));
                    return deliveryOrderView;
                }).sorted(Comparator.comparing(DeliveryOrderView::getStatus)).collect(Collectors.toList());
    }

}
