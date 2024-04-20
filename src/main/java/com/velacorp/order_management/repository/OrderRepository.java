package com.velacorp.order_management.repository;

import com.velacorp.order_management.entity.Orders;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
  @Query("SELECT o FROM Orders o WHERE (:id IS NULL OR o.id = :id) AND LOWER(o.customerName) LIKE LOWER(CONCAT('%', COALESCE(:keyword, ''), '%'))")
  List<Orders> findByOrderIdOrCustomerNameContainingIgnoreCase(Long id, String keyword);

}
