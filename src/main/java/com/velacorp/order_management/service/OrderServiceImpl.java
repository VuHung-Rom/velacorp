package com.velacorp.order_management.service;

import com.velacorp.order_management.common.CommonException;
import com.velacorp.order_management.entity.Order;
import com.velacorp.order_management.entity.dto.BaseResponse;
import com.velacorp.order_management.repository.OrderRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService{

  @Autowired
  private OrderRepository orderRepository;

  @Override
  public List<Order> getAllOrders() throws Exception {
    BaseResponse response = new BaseResponse();
    try {
      List<Order> orders = orderRepository.findAll();
      return orders;
    } catch (Exception exception) {
      response.setResponseCode("ERR_GET_ALL_ORDER_FAIL");
      response.setMessage("Cannot retrieve order");
      throw new CommonException(exception, "ERR_GET_ALL_ORDER_FAIL", "Cannot retrieve order");
    }
  }
}
