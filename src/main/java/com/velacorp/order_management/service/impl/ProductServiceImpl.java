package com.velacorp.order_management.service.impl;

import com.velacorp.order_management.common.CommonException;
import com.velacorp.order_management.entity.Product;
import com.velacorp.order_management.entity.dto.BaseResponse;
import com.velacorp.order_management.entity.dto.ProductDTO;
import com.velacorp.order_management.repository.ProductRepository;
import com.velacorp.order_management.service.ProductService;
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
    BaseResponse response = new BaseResponse();
    try {
      if (keyword == null || keyword.isBlank()) {
        return productRepository.findAll();
      }

      return productRepository.findByProductNameContainingOrDescriptionContaining(keyword, keyword);
    } catch (Exception exception) {
      response.setResponseCode("ERR_SEARCH_PRODUCTS_FAIL");
      response.setMessage("Cannot retrieve products");
      throw new CommonException(exception, "ERR_SEARCH_PRODUCTS_FAIL", "Cannot retrieve products");
    }
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
    BeanUtils.copyProperties(requestProduct, product);
    product.setStatus("1");
    return productRepository.save(product);
  }

  /**
   *
   * @param requestProduct
   * @throws CommonException
   */
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

  /**
   *
   * @param productId
   * @param requestProduct
   * @return
   */
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
    Optional<Product> productOptional = productRepository.findById(productId);
    if (productOptional.isPresent()) {
      Product product = productOptional.get();
      product.setStatus("-1");
      productRepository.save(product);
    } else {
      throw new CommonException("Product with id " , " not found");
    }
  }
}
