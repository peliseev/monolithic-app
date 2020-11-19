package ru.skillbox.monolithicapp.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification() {
        System.out.println("Notification was send");
    }
}
