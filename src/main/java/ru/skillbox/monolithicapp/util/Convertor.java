package ru.skillbox.monolithicapp.util;

import ru.skillbox.monolithicapp.entity.Order;
import ru.skillbox.monolithicapp.entity.OrderItem;
import ru.skillbox.monolithicapp.model.DeliveryOrderView;
import ru.skillbox.monolithicapp.model.EOrderStatus;
import ru.skillbox.monolithicapp.model.ItemView;

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

    public static List<DeliveryOrderView> orderToDeliveryOrder(List<Order> order) {
        return order.stream()
                .map(o -> {
                    DeliveryOrderView deliveryOrderView = new DeliveryOrderView();
                    deliveryOrderView.setId(o.getId());
                    deliveryOrderView.setStatus(o.getStatus());
                    deliveryOrderView.setStatusText(o.getStatus().getHumanReadable());
                    deliveryOrderView.setCustomerFullName(
                            o.getCustomer().getFirstName() + " " + o.getCustomer().getLastName());
                    deliveryOrderView.setCustomerAddress(o.getCustomer().getAddress());
                    if (o.getStatus() == EOrderStatus.ORDER_COMING) {
                        deliveryOrderView.setCourierFullName(
                                o.getCourier().getFirstName() + " " + o.getCourier().getLastName());
                    }
                    deliveryOrderView.setItems(o.getItems().stream()
                            .map(Convertor::orderItemToItemView)
                            .collect(Collectors.toList()));
                    return deliveryOrderView;
                }).sorted(Comparator.comparing(DeliveryOrderView::getStatus)).collect(Collectors.toList());
    }

}
