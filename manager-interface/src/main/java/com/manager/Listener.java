package com.manager;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Listener {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void receiveMessage(Long message) {

        System.out.println("Confirm order " +  message + " ? y/n");
        Scanner scanner = new Scanner(System.in);

        String result = scanner.next();

        if (result.equals("y")){
            rabbitTemplate.convertAndSend(Interface.orderConfirmExchange, "order.confirmed", message);
        }

    }

}