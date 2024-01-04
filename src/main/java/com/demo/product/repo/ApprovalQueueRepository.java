package com.demo.product.repo;

import com.demo.product.model.ApprovalQueue;
import com.demo.product.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalQueueRepository extends JpaRepository<ApprovalQueue, Long> {

    public ApprovalQueue save(ApprovalQueue approvalQueue);
    public List<ApprovalQueue> findAllByOrderByRequestedDateDesc();

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update ApprovalQueue aq set aq.id=:id,aq.prodId= :prodId, aq.status= 'approved' where aq.id= :id ")
    public  void approveProduct(Long id,Long prodId);


    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update ApprovalQueue aq set aq.id=:id,aq.prodId= :prodId, aq.status= 'rejected' where aq.id= :id ")
    public  void rejectProduct(Long id,Long prodId);



}
