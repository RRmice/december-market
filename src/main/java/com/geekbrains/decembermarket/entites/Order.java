package com.geekbrains.decembermarket.entites;

import com.geekbrains.decembermarket.beans.Cart;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "confirm_order")
    private int confirmed;

    public Order(User user, Cart cart, String address, String phone) {
        this.user = user;
        this.price = cart.getPrice();
        this.items = new ArrayList<>();
        this.address = address;
        this.phone = phone;
        for (OrderItem i : cart.getItems()) {
            i.setOrder(this);
            this.items.add(i);
        }

        this.confirmed = 0;

        cart.clear();
    }

    public Long getId(){
        return id;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

}
