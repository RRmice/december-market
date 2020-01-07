package com.geekbrains.decembermarket.repositories;


import com.geekbrains.decembermarket.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByPhone(String phone);
    boolean existsByPhone(String phone);
}