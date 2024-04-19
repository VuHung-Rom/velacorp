package com.velacorp.order_management.service;

import com.velacorp.order_management.common.CommonException;
import com.velacorp.order_management.entity.Product;
import com.velacorp.order_management.entity.dto.BaseResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.velacorp.order_management.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Override
  public List<Product> getAllProducts() throws Exception {
    BaseResponse response = new BaseResponse();
    try {
      List<Product> products = productRepository.findAll();
      return products;
    } catch (Exception exception) {
      response.setResponseCode("ERR_GET_ALL_PRODUCTS_FAIL");
      response.setMessage("Cannot retrieve products");
      throw new CommonException(exception, "ERR_GET_ALL_PRODUCTS_FAIL", "Cannot retrieve products");
    }
  }
}
