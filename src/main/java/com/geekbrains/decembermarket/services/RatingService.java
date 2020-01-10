package com.geekbrains.decembermarket.services;

import com.geekbrains.decembermarket.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private RatingRepository ratingRepository;

    @Autowired
    public void setRatingRepository(RatingRepository ratingRepository){
        this.ratingRepository = ratingRepository;
    }

}
