package com.demo.product.repo;

import com.demo.product.model.Product;
import com.demo.product.model.RejectedProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RejectedProductsRepository extends JpaRepository<RejectedProducts, Long> {
}
