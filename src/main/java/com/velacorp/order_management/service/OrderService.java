package com.velacorp.order_management.service;

import com.velacorp.order_management.entity.Orders;
import java.util.List;

public interface OrderService {
  List<Orders> getAllOrders() throws Exception;

}
