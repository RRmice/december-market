package com.geekbrains.decembermarket.controllers;

import com.geekbrains.decembermarket.beans.Cart;
import com.geekbrains.decembermarket.entites.Category;
import com.geekbrains.decembermarket.entites.Order;
import com.geekbrains.decembermarket.entites.Product;
import com.geekbrains.decembermarket.entites.User;
import com.geekbrains.decembermarket.services.CategoryService;
import com.geekbrains.decembermarket.services.OrderService;
import com.geekbrains.decembermarket.services.ProductService;
import com.geekbrains.decembermarket.services.UserService;
import com.geekbrains.decembermarket.utils.ProductFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class MarketController {
    private ProductService productService;
    private CategoryService categoryService;
    private UserService userService;
    private OrderService orderService;


    public MarketController(ProductService productService, CategoryService categoryService,
                            UserService userService, OrderService orderService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login_page";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }

        User user = userService.findByPhone(principal.getName());
        //List<Order> orders = orderService.getOrdersByUser(user);
        List<Order> orders = orderService.getOrderByPhone(user.getPhone());


        if (!userService.isUserConfirm(user)){
            model.addAttribute("pin", userService.getConfirmKeys(user));
        }

        model.addAttribute("user", user);
        model.addAttribute("orders", orders);

        return "profile";
    }

    @GetMapping("/")
    public String index(Model model, @RequestParam Map<String, String> params) {
        int pageIndex = 0;
        if (params.containsKey("p")) {
            pageIndex = Integer.parseInt(params.get("p")) - 1;
        }
        Pageable pageRequest = PageRequest.of(pageIndex, 10);
        ProductFilter productFilter = new ProductFilter(params);
        Page<Product> page = productService.findAll(productFilter.getSpec(), pageRequest);

        List<Category> categories = categoryService.getAll();
        model.addAttribute("filtersDef", productFilter.getFilterDefinition());
        model.addAttribute("categories", categories);
        model.addAttribute("page", page);
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(Model model, @PathVariable Long id) {
        Product product = productService.findById(id);
        List<Category> categories = categoryService.getAll();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "edit_product";
    }

    @PostMapping("/edit")
    public String saveProduct(@ModelAttribute(name = "product") Product product) {
        productService.save(product);
        return "redirect:/";
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration_page";
    }

    @PostMapping("/registration/confirm")
    public String confirmUserRegistration(Principal principal, @RequestParam(name = "pin") String pin){

        User user = userService.findByPhone(principal.getName());
        userService.confirmUser(user, pin);
        return  "redirect:/";
    }

    @PostMapping("/registration/user")
    public String userRegistration(@ModelAttribute(name = "user") User user){
        userService.createNewUser(user);
        return  "redirect:/";
    }
}