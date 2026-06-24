package br.com.riverfy.consumer;

import br.com.riverfy.config.RabbitMQConfig;
import br.com.riverfy.model.User;
import br.com.riverfy.repository.UserRepository;
import br.com.riverfy.service.notification.EmailNotification;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationConsumer {

    private final UserRepository userRepository;
    private final EmailNotification emailNotification;

    public NotificationConsumer(UserRepository userRepository, EmailNotification emailNotification) {
        this.userRepository = userRepository;
        this.emailNotification = emailNotification;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeNewEventNotification(String message) {
        List<User> activeUsers = userRepository.findAll();

        for (User user : activeUsers) {

            try {
                emailNotification.send(
                        user.getEmail(),
                        "Novo Evento na IERV!",
                        "Olá " + user.getName() + "!\n\n" + message
                );
            } catch (Exception e) {
               throw new RuntimeException(e);
            }

        }
    }
}
