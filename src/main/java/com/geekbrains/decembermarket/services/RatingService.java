package com.geekbrains.decembermarket.services;

import com.geekbrains.decembermarket.entites.Product;
import com.geekbrains.decembermarket.entites.Rating;
import com.geekbrains.decembermarket.entites.User;
import com.geekbrains.decembermarket.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private RatingRepository ratingRepository;

    @Autowired
    public void setRatingRepository(RatingRepository ratingRepository){
        this.ratingRepository = ratingRepository;
    }

    public Integer getRating(User user, Product product){
        return ratingRepository.getRatingByUserAndProduct(user, product);
    }

    public void saveRating(Rating rating){
        ratingRepository.save(rating);
    }

    public List<Rating> getRatingByProduct(Product product){
        return ratingRepository.getAllByProduct(product);
    }


}
