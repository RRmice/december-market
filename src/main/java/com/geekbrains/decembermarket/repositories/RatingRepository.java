package com.geekbrains.decembermarket.repositories;

import com.geekbrains.decembermarket.entites.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
