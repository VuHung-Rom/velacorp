package com.velacorp.order_management.service;

import com.velacorp.order_management.entity.Product;
import java.util.List;

public interface ProductService {
  List<Product> getAllProducts() throws Exception;

}
