package com.example.AddressBookAppWorkShop.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_REGISTRATION_QUEUE = "user.registration.queue";
    public static final String CONTACT_ADDED_QUEUE = "contact.added.queue";
    public static final String EXCHANGE = "addressbook.exchange";

    @Bean
    public Queue userRegistrationQueue() {
        return new Queue(USER_REGISTRATION_QUEUE, true);
    }

    @Bean
    public Queue contactAddedQueue() {
        return new Queue(CONTACT_ADDED_QUEUE, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding bindUserRegistrationQueue(Queue userRegistrationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(userRegistrationQueue).to(exchange).with("user.register");
    }

    @Bean
    public Binding bindContactAddedQueue(Queue contactAddedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(contactAddedQueue).to(exchange).with("contact.add");
    }
}
