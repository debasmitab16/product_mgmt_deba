package com.demo.product.service;

import com.demo.product.model.RejectedProducts;
import com.demo.product.repo.RejectedProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RejectedProductServiceImpl implements  RejectedProductService{
   @Autowired
    RejectedProductsRepository rejectedProductsRepository;
    @Override
    public RejectedProducts save(RejectedProducts prd) {
        return rejectedProductsRepository.save(prd);
    }
}
