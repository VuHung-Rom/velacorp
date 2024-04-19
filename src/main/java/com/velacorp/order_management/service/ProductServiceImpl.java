package com.velacorp.order_management.service;

import com.velacorp.order_management.common.CommonException;
import com.velacorp.order_management.entity.Product;
import com.velacorp.order_management.entity.dto.BaseResponse;
import com.velacorp.order_management.entity.dto.ProductDTO;
import com.velacorp.order_management.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  @Override
  public List<Product> searchProducts(String keyword) {
    if (keyword == null || keyword.isBlank()) {
      return productRepository.findAll();
    }

    return productRepository.findByProductNameContainingOrDescriptionContaining(keyword, keyword);
  }


  @Override
  public Optional<Product> getProductById(Long id) throws Exception {
    BaseResponse response = new BaseResponse();
    try {
      Optional<Product> product = productRepository.findById(id);
      return product;
    } catch (Exception exception) {
      response.setResponseCode("ERR_GET_PRODUCT_BY_ID_FAIL");
      response.setMessage("Cannot retrieve product");
      throw new CommonException(exception, "ERR_GET_PRODUCT_BY_ID_FAIL", "Cannot retrieve product");
    }
  }

  @Override
  public Product createProduct(ProductDTO requestProduct) throws Exception {
    validateRequiredFields(requestProduct);

    Product product = new Product();
    product.setProductName(requestProduct.getProductName());
    product.setDescription(requestProduct.getDescription());
    product.setPrice(requestProduct.getPrice());
    product.setStockQuantity(requestProduct.getStockQuantity());
    return productRepository.save(product);
  }

  private void validateRequiredFields(ProductDTO requestProduct) throws CommonException {
    if (requestProduct.getProductName() == null || requestProduct.getProductName().isEmpty()) {
      throw new CommonException("2", "Product name is required.");
    }

    if (requestProduct.getDescription() == null || requestProduct.getDescription().isEmpty()) {
      throw new CommonException("2", "Description is required.");
    }

    if (requestProduct.getPrice() == null) {
      throw new CommonException("2", "Price is required.");
    }

    if (requestProduct.getStockQuantity() == null) {
      throw new CommonException("2", "Stock quantity is required.");
    }
  }

  @Override
  public Product updateProduct(Long productId, ProductDTO requestProduct) {
    Optional<Product> optionalProduct = productRepository.findById(productId);
    if (optionalProduct.isPresent()) {
      Product product = optionalProduct.get();
      BeanUtils.copyProperties(requestProduct, product);
      return productRepository.save(product);
    } else {
      throw new CommonException("Product with id ", " not found");
    }
  }

  @Override
  public void deleteProduct(Long productId) {
    if (productRepository.existsById(productId)) {
      productRepository.deleteById(productId);
    } else {
      throw new CommonException("Product with id ", " not found");
    }
  }
}
