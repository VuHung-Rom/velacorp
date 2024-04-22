package com.velacorp.order_management.service;

import com.velacorp.order_management.entity.Product;
import com.velacorp.order_management.entity.dto.ProductDTO;
import com.velacorp.order_management.entity.dto.ProductResponse;
import java.util.List;
import java.util.Optional;

public interface ProductService {
  List<Product> getAllProducts() throws Exception;
  Optional<Product> getProductById(Long id) throws Exception;
  Product createProduct(ProductDTO requestProduct) throws Exception;
  Product updateProduct(Long id, ProductDTO requestProduct) throws Exception;
  void deleteProduct(Long productId) throws Exception;
  ProductResponse searchProducts(String keyword ,int pageSize, int pageNumber) throws Exception;
}
