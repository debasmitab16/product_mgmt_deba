package com.demo.product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="rejected_products")
public class RejectedProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long prodId;
    private String prodName;
    private Double price;
    private String status;
    private LocalDate rejectionDate;
}
