package com.velacorp.order_management.repository;

import com.velacorp.order_management.entity.OrderDetail;
import com.velacorp.order_management.entity.Orders;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
  List<OrderDetail> findByOrderAndStatusEquals(Orders order, String status );

}
