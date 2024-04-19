package com.velacorp.order_management.service;

import com.velacorp.order_management.entity.Order;
import java.util.List;

public interface OrderService {
  List<Order> getAllOrders() throws Exception;

}
