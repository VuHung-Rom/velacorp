package com.velacorp.order_management.repository;

import com.velacorp.order_management.entity.Orders;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
  @Query(value ="SELECT o.* FROM orders o WHERE (:id IS NULL OR o.id = :id) "
      + "AND LOWER(o.customer_name) LIKE LOWER(CONCAT('%', COALESCE(:keyword, ''), '%')) "
      + "AND o.status = '1' "
      + "ORDER BY order_date desc LIMIT :pageSize OFFSET :offset "
      ,nativeQuery = true)
  List<Orders> findByOrderIdOrCustomerNameContainingIgnoreCase(Long id, String keyword, Integer pageSize, Integer offset);
  @Query(value ="SELECT COUNT(*) FROM orders o WHERE (:id IS NULL OR o.id = :id) "
      + "AND LOWER(o.customer_name) LIKE LOWER(CONCAT('%', COALESCE(:keyword, ''), '%')) "
      + "AND o.status = '1' "
      ,nativeQuery = true)
  Long findByOrderIdOrCustomerNameContainingIgnoreCaseCount(Long id, String keyword);

}
