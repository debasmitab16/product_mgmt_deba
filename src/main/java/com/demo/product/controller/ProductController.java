package com.demo.product.controller;

import com.demo.product.exception.ProductNotFoundException;
import com.demo.product.exception.ProductPriceLimitExceedsException;
import com.demo.product.model.ApprovalQueue;
import com.demo.product.model.Product;
import com.demo.product.model.RejectedProducts;
import com.demo.product.repo.ApprovalQueueRepository;
import com.demo.product.repo.ProductRepository;
import com.demo.product.service.ApprovalQueueService;
import com.demo.product.service.ProductService;
import com.demo.product.service.RejectedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ApprovalQueueService approvalQueueService;

    @Autowired
    private ProductRepository productRepository;
    private ApprovalQueue approvalQueue=new ApprovalQueue();

    @Autowired
    private ApprovalQueueRepository approvalQueueRepository;

    @Autowired
    private RejectedProductService rejectedProductService;


    ProductPriceLimitExceedsException productPriceLimitExceedsException=new ProductPriceLimitExceedsException("Product amount must be less than 10000$");



    @GetMapping(value="/products")
    List<Product> getAllProducts() {

        return productService.getAllProducts();
    }

    @GetMapping(value="/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("query") Optional<String> query, @RequestParam("minPrice") Optional<Double> minPrice, @RequestParam("maxPrice")Optional<Double> maxPrice, @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate>startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate>endDate){

        if(query.isPresent()){
          return ResponseEntity.ok(productService.searchProducts(query));
        }
        else if(minPrice.isPresent()&&maxPrice.isPresent()) {
            return ResponseEntity.ok(productService.searchPriceRange(minPrice, maxPrice));
        }
        else if(startDate.isPresent()&&endDate.isPresent()){
            return ResponseEntity.ok(productService.searchByDateRange(startDate,endDate));
        }
        return new ResponseEntity<List<Product>>( HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) throws ProductPriceLimitExceedsException {
        if(product.getPrice()>10000){
            throw productPriceLimitExceedsException;
        }
        if(product.getPrice()<=5000) {
            product.setPostedDate(LocalDate.now());
            productService.save(product);

        }
        else{
            approvalQueue.setStatus(product.getStatus());
            approvalQueue.setProdId(product.getId());
            approvalQueue.setProdName(product.getProdName());
            approvalQueue.setPrice(product.getPrice());
            approvalQueue.setRequestedDate(LocalDate.now());
            approvalQueueService.save(approvalQueue);
        }
        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }


    @PutMapping("/products/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) throws ProductNotFoundException {

        Product prd=productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"+id));
        Double prevPrice=prd.getPrice();
        System.out.println("Prev price ...."+prevPrice);
        Double price=product.getPrice();


        System.out.println(" price ...."+price);
        Double updatefiftyPercent=prevPrice+prevPrice/2;
        System.out.println(" updatefiftyPercent ...."+updatefiftyPercent);
       if(price<=updatefiftyPercent){
            productService.update(product);
        }
        else {
            approvalQueue.setStatus(prd.getStatus());
            approvalQueue.setProdId(prd.getId());
            approvalQueue.setRequestedDate(LocalDate.now());
            approvalQueue.setProdName(prd.getProdName());
            approvalQueue.setPrice(prd.getPrice());
            productService.delete(id);
            approvalQueueService.save(approvalQueue);

        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable("id") Long id) throws ProductNotFoundException {
        // Delete the user in this method with the id.
        Product product=productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"+id));
        productService.delete(id);
        approvalQueue.setProdName(product.getProdName());
        approvalQueue.setProdId(id);
        approvalQueue.setPrice(product.getPrice());
        approvalQueue.setStatus(product.getStatus());
        

        approvalQueueService.save(approvalQueue);
    }

    @GetMapping(value="/products/approval-queue")
    List<ApprovalQueue> getAllQueues(){
        return approvalQueueService.getAllQueues();
    }

    @PutMapping("/products/approval-queue/{id}/approve")
    public ResponseEntity<ApprovalQueue> approveQueue(@PathVariable("id") Long id, @RequestBody ApprovalQueue approvalQueue1) throws ProductNotFoundException {
      ApprovalQueue aq=approvalQueueRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"+id));
        approvalQueueService.approveProduct(approvalQueue1);
        System.out.println("approve method called");
        System.out.println("approvalQueue1.getId()......."+approvalQueue1.getId());
        approvalQueueService.delete(id);
        System.out.println("delete method called");

        Product prd=new Product();
        prd.setId(approvalQueue1.getProdId());
        prd.setStatus(approvalQueue1.getStatus());
        prd.setPrice(approvalQueue1.getPrice());
        prd.setProdName(approvalQueue1.getProdName());
        prd.setPostedDate(LocalDate.now());

        System.out.println("prod id...."+prd.getId());
        System.out.println("prod status...."+prd.getStatus());
        productService.save(prd);

        return new ResponseEntity<>(approvalQueue1, HttpStatus.OK);
    }

    @PutMapping("/products/approval-queue/{id}/reject")
    public ResponseEntity<ApprovalQueue> rejectQueue(@PathVariable("id") Long id, @RequestBody ApprovalQueue approvalQueue1) throws ProductNotFoundException {
        ApprovalQueue aq=approvalQueueRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"+id));
        approvalQueueService.rejectProduct(approvalQueue1);
        System.out.println("reject method called");
        System.out.println("approvalQueue1.getId()......."+approvalQueue1.getId());
        approvalQueueService.delete(id);
        System.out.println("delete method called");

        RejectedProducts rejectedProducts=new RejectedProducts();
        rejectedProducts.setProdId(approvalQueue1.getProdId());
        rejectedProducts.setProdName(approvalQueue1.getProdName());
        rejectedProducts.setPrice(approvalQueue1.getPrice());
        rejectedProducts.setStatus(approvalQueue1.getStatus());
        rejectedProducts.setRejectionDate(LocalDate.now());

        rejectedProductService.save(rejectedProducts);
        return new ResponseEntity<>(approvalQueue1, HttpStatus.OK);
    }


}
