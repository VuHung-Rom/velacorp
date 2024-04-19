package com.velacorp.order_management.controller;

import com.velacorp.order_management.entity.Order;
import com.velacorp.order_management.entity.Product;
import com.velacorp.order_management.entity.dto.BaseResponse;
import com.velacorp.order_management.service.OrderService;
import com.velacorp.order_management.service.ProductService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
  @Autowired
  private OrderService orderService;

  private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

  @GetMapping
  public ResponseEntity<BaseResponse> getAllOrders() {
    BaseResponse response = new BaseResponse();
    try {
      List<Order> orders = orderService.getAllOrders();
      response.setResponseCode("0");
      response.setMessage("Success");
      response.setData(orders);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
      logger.error("An error occurred while getting all order", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(response);
    }
  }
}
