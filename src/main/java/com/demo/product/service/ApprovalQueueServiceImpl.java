package com.demo.product.service;

import com.demo.product.model.ApprovalQueue;
import com.demo.product.model.Product;
import com.demo.product.repo.ApprovalQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalQueueServiceImpl implements ApprovalQueueService{

@Autowired
private ApprovalQueueRepository approvalQueueRepository;

    @Override
    public ApprovalQueue save(ApprovalQueue approvalQueue) {

            approvalQueueRepository.save(approvalQueue);
            System.out.println("Call after save...........");

        return approvalQueue;
    }



    @Override
    public List<ApprovalQueue> getAllQueues() {
        return approvalQueueRepository.findAllByOrderByRequestedDateDesc();
    }

    @Override
    public void approveProduct(ApprovalQueue approvalQueue) {
         approvalQueueRepository.approveProduct(approvalQueue.getId(),approvalQueue.getProdId());
    }

    @Override
    public void rejectProduct(ApprovalQueue approvalQueue) {
        approvalQueueRepository.rejectProduct(approvalQueue.getId(),approvalQueue.getProdId());
    }

    @Override
    public void delete(Long id) {
        approvalQueueRepository.deleteById(id);
    }
}
