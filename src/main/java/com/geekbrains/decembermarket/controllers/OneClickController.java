package com.geekbrains.decembermarket.controllers;

import com.geekbrains.decembermarket.beans.Cart;
import com.geekbrains.decembermarket.entites.Order;
import com.geekbrains.decembermarket.entites.User;
import com.geekbrains.decembermarket.services.OrderService;
import com.geekbrains.decembermarket.services.ProductService;
import com.geekbrains.decembermarket.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/1click")
public class OneClickController {

    private ProductService productService;
    private Cart cart;
    private UserService userService;
    private OrderService orderService;

    @Autowired
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Autowired
    public void setProductService(ProductService productService){
        this.productService = productService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/cart/add/{id}")
    public void addProductToCart(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        cart.add(productService.findById(id));
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("/order/info")
    public String showOrderInfo(Model model, Principal principal) {
        model.addAttribute("cart", cart);
        return "order_info_before_confirmation";
    }

    @PostMapping("/orders/create")
    public String createOrder(Model model,
                              @RequestParam(name = "address") String address,
                              @RequestParam("phone_number") String phone) {

        Order order = new Order(userService.findByPhone("One click user"), cart, address, phone);
        order = orderService.save(order);
        model.addAttribute("order_id_str", String.format("%05d", order.getId()));
        return "order_confirmation.html";
    }

}