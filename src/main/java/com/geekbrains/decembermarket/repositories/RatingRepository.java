package com.geekbrains.decembermarket.repositories;

import com.geekbrains.decembermarket.entites.Product;
import com.geekbrains.decembermarket.entites.Rating;
import com.geekbrains.decembermarket.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Integer getRatingByUserAndProduct(User user, Product product);
    List<Rating> getAllByProduct(Product product);
}
