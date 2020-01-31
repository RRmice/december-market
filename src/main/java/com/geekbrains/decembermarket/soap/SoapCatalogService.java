package com.geekbrains.decembermarket.soap;

import com.geekbrains.decembermarket.soap.catalog.ProductDto;
import com.geekbrains.decembermarket.soap.catalog.ProductsList;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class SoapCatalogService {
    private List<ProductDto> productDtos;

    @PostConstruct
    public void init() {
        productDtos = new ArrayList<>();
        ProductDto productDto1 = new ProductDto();
        productDto1.setTitle("Product #1");
        productDtos.add(productDto1);
        ProductDto productDto2 = new ProductDto();
        productDto2.setTitle("Product #2");
        productDtos.add(productDto2);
    }

    public ProductsList getAllProductsList() {
        ProductsList productsList = new ProductsList();
        productsList.getProduct().addAll(productDtos);
        return productsList;
    }
}
