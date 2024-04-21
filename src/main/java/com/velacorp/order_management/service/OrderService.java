package com.velacorp.order_management.service;

import com.velacorp.order_management.entity.OrderDetail;
import com.velacorp.order_management.entity.Orders;
import com.velacorp.order_management.entity.Product;
import com.velacorp.order_management.entity.dto.OrderDTO;
import com.velacorp.order_management.entity.dto.OrdersResponse;
import com.velacorp.order_management.entity.dto.ProductDTO;
import java.util.List;
import java.util.Optional;

public interface OrderService {
  List<Orders> getAllOrders() throws Exception;
  OrdersResponse searchOrders(Long id, String keyword, int pageSize, int pageNumber) throws Exception;
  Orders createOrders(OrderDTO orderDTO) throws Exception;
  Orders updateOrder(Long orderId, OrderDTO requestOrder) throws Exception;
  Optional<OrderDetail> getOrderDetailById(Long id) throws Exception;

}
