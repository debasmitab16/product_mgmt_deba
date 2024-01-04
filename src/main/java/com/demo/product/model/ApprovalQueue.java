package com.demo.product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="approval_queue")
public class ApprovalQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private Long prodId;
    private LocalDate requestedDate;
    private String prodName;
    private Double price;






//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "prodId", referencedColumnName = "id")


}
