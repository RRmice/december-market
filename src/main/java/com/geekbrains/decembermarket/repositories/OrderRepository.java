package com.geekbrains.decembermarket.repositories;

import com.geekbrains.decembermarket.entites.Order;
import com.geekbrains.decembermarket.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User User);

}