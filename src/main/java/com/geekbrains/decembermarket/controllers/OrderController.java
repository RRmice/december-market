package com.geekbrains.decembermarket.controllers;

import com.geekbrains.decembermarket.DecemberMarketApplication;
import com.geekbrains.decembermarket.beans.Cart;
import com.geekbrains.decembermarket.entites.Order;
import com.geekbrains.decembermarket.entites.User;
import com.geekbrains.decembermarket.services.OrderService;
import com.geekbrains.decembermarket.services.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private UserService userService;
    private OrderService orderService;
    private Cart cart;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    private void setRabbitTemplate(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public OrderController(UserService userService, OrderService orderService, Cart cart) {
        this.userService = userService;
        this.orderService = orderService;
        this.cart = cart;
    }

    @GetMapping("/info")
    public String showOrderInfo(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.findByPhone(principal.getName());
            model.addAttribute("def_phone", user.getPhone());
        }
        model.addAttribute("cart", cart);
        return "order_info_before_confirmation";
    }

    @PostMapping("/create")
    public String createOrder(Principal principal, Model model, @RequestParam(name = "address") String address, @RequestParam("phone_number") String phone) {
        User user = null;
        if (principal != null) {
            user = userService.findByPhone(principal.getName());
        } else {
            user = userService.getAnonymousUser();
        }
        Order order = new Order(user, cart, address, phone);
        order = orderService.save(order);

        rabbitTemplate.convertAndSend(DecemberMarketApplication.orderConfirmExchange, "order.confirm", order.getId());

        model.addAttribute("order_id_str", String.format("%05d", order.getId()));
        return "order_confirmation";
    }

    @GetMapping("/history")
    public String showHistory(Model model, Principal principal) {
        User user = userService.findByPhone(principal.getName());
        model.addAttribute("username", user.getFullName());
        model.addAttribute("orders", user.getOrders());
        return "orders_history";
    }

    @PostMapping("/confirm/{id}")
    public void confirmOrder(@PathVariable Long id){

        orderService.confirmOrder(id);

    }
}
