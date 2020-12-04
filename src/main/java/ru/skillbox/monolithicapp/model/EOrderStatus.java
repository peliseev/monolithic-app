package ru.skillbox.monolithicapp.model;

public enum EOrderStatus {
    ORDER_CREATED("Создан"),
    ORDER_PAID("Оплачен"),
    ORDER_COMING("Ожидайте доставки"),
    ORDER_DELIVERED("Доставлен");

    private final String humanReadableText;

    EOrderStatus(String humanReadableText) {
        this.humanReadableText = humanReadableText;
    }

    public String getHumanReadableText() {
        return humanReadableText;
    }
}
