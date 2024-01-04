package com.demo.product.service;

import com.demo.product.model.Product;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();

   List<Product> searchProducts(Optional<String> query);
   List<Product> searchPriceRange(Optional<Double> minPrice,Optional<Double>maxPrice);

    public List<Product> searchByDateRange(Optional<LocalDate>startDate, Optional<LocalDate>endDate );

    Product save(Product prd);

    void update(Product prd);
    void delete(Long id);
}
