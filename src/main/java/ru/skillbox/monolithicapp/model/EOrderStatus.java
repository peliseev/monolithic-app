package ru.skillbox.monolithicapp.model;

public enum EOrderStatus {
    ORDER_CREATED("Создан"),
    ORDER_PAID("Оплачен"),
    ORDER_DELIVER("Ожидайте доставки"),
    ORDER_DELIVERED("Доставлен");

    private final String humanReadable;

    EOrderStatus(String humanReadable) {
        this.humanReadable = humanReadable;
    }

    public String getHumanReadable() {
        return humanReadable;
    }
}
