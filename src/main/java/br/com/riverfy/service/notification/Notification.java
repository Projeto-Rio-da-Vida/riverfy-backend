package br.com.riverfy.service.notification;

public interface Notification {
    void send(String destination, String subject, String message);
}
