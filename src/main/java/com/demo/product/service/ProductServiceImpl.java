package com.demo.product.service;

import com.demo.product.model.Product;
import com.demo.product.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Product> getAllProducts() {
        // TODO Auto-generated method stub
        return productRepository.findAllByOrderByPostedDateDesc();
    }

    @Override
    public List<Product> searchProducts(Optional<String> query) {
        List<Product> productList = productRepository.searchProducts(query);
        return productList;
    }

    @Override
    public List<Product> searchPriceRange(Optional<Double> minPrice, Optional<Double> maxPrice) {
        return productRepository.searchByPriceRange(minPrice,maxPrice);
    }


    @Override
    public List<Product> searchByDateRange(Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
        return productRepository.searchByDateRange(startDate,endDate);
    }
    @Override
    public Product save(Product prd) {

        return productRepository.save(prd);
    }

    @Override
    public void update(Product prd) {
         productRepository.updateProduct(prd.getId(),prd.getPrice(),prd.getProdName(),prd.getStatus() );
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        productRepository.deleteById(id);
    }
}
