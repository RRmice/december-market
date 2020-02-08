package com.geekbrains.decembermarket.beans;

import com.geekbrains.decembermarket.services.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    private RabbitTemplate rabbitTemplate;
    private OrderService orderService;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setOrderService(OrderService orderService){
        this.orderService = orderService;
    }

    public void receiveMessage(Long message) {

        orderService.confirmOrder(message);
        System.out.println(message);

    }

}