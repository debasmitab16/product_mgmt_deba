package com.demo.product.repo;

import com.demo.product.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findAllByOrderByPostedDateDesc();
    @Query("SELECT p FROM Product p WHERE " +
            "p.prodName LIKE CONCAT('%',:query, '%')" )
    public List<Product> searchProducts(Optional<String> query);
    @Query("select p from Product p where p.price between :minPrice and :maxPrice")
    public List<Product> searchByPriceRange(Optional<Double>minPrice,Optional<Double>maxPrice);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Product p set p.id=:id, p.price= :price, p.prodName= :name, p.status= :status where p.id= :id ")
    public  void updateProduct(Long id,Double price, String name,String status);

    @Query("select p from Product p where p.postedDate between :startDate and :endDate")
    public List<Product> searchByDateRange(Optional<LocalDate>startDate, Optional<LocalDate>endDate );
}
