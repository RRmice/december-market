package com.geekbrains.decembermarket.services;

import com.geekbrains.decembermarket.entites.Order;
import com.geekbrains.decembermarket.entites.User;
import com.geekbrains.decembermarket.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private OrderRepository orderRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

//    public List<Order> getOrdersByUser(User user){
//        return orderRepository.findByUser(user);
//    }

    public List<Order> getOrderByPhone(String phone){
        return orderRepository.findByPhone(phone);
    }

}
