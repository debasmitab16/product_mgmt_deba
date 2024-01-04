package com.demo.product.service;

import com.demo.product.model.ApprovalQueue;
import com.demo.product.model.Product;

import java.util.List;

public interface ApprovalQueueService {
    ApprovalQueue save(ApprovalQueue approvalQueue);



    List<ApprovalQueue> getAllQueues();

    void approveProduct(ApprovalQueue approvalQueue);
    void rejectProduct(ApprovalQueue approvalQueue);

    void delete(Long id);
}
