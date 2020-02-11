package com.manager;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Interface {

    public static final String orderConfirmQueue = "order-confirm-queue";
    public static final String orderConfirmed = "order-confirmed-queue";
    public static final String orderConfirmExchange = "order-exchange";

    @Bean
    Queue orderConfirmQueue() {
        return new Queue(orderConfirmQueue, false);
    }

    @Bean
    Queue orderConfirmedQueue() {
        return new Queue(orderConfirmed, false);
    }

    @Bean
    TopicExchange orderExchange() {
        return new TopicExchange(orderConfirmExchange);
    }

    @Bean
    Binding bindingTopicIn(@Qualifier("orderConfirmQueue") Queue queue, TopicExchange orderExchange) {
        return BindingBuilder.bind(queue).to(orderExchange).with("order.confirm");
    }

    @Bean
    Binding bindingTopicOut(@Qualifier("orderConfirmedQueue") Queue queue, TopicExchange orderExchange) {
        return BindingBuilder.bind(queue).to(orderExchange).with("order.confirmed");
    }


    @Bean
    SimpleMessageListenerContainer containerForTopic(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("order-confirm-queue");
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Listener listener) {
        return new MessageListenerAdapter(listener, "receiveMessage");
    }

    public static void main(String[] args) {
        SpringApplication.run(Interface.class, args);
    }


}