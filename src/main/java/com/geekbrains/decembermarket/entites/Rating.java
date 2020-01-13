package com.geekbrains.decembermarket.entites;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "product_rating")
@Entity
@Data
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "rating")
    private Integer rating;

}
