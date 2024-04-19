package com.velacorp.order_management.controller;

import com.velacorp.order_management.service.ProductService;
import com.velacorp.order_management.entity.Product;
import com.velacorp.order_management.entity.dto.BaseResponse;
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
@RequestMapping("/api/v1/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

  @GetMapping
  public ResponseEntity<BaseResponse> getAllProducts() {
    BaseResponse response = new BaseResponse();
    try {
      List<Product> products = productService.getAllProducts();
      response.setResponseCode("0");
      response.setMessage("Success");
      response.setData(products);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
      logger.error("An error occurred while getting all products", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(response);
    }
  }
}
