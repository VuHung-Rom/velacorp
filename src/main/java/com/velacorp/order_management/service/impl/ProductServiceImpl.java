package com.velacorp.order_management.service.impl;

import com.velacorp.order_management.common.CommonException;
import com.velacorp.order_management.common.Utils.Constants;
import com.velacorp.order_management.entity.Product;
import com.velacorp.order_management.entity.dto.BaseResponse;
import com.velacorp.order_management.entity.dto.ProductDTO;
import com.velacorp.order_management.repository.ProductRepository;
import com.velacorp.order_management.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Override
  public List<Product> getAllProducts() throws Exception {
    try {
      List<Product> products = productRepository.findAll();
      return products;
    } catch (Exception exception) {
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
      Optional<Product> product = productRepository.findById(id);
      return product;

  }

  @Override
  @Transactional
  public Product createProduct(ProductDTO requestProduct) throws Exception {
    validateRequiredFields(requestProduct);
    Product product = new Product();
    BeanUtils.copyProperties(requestProduct, product);
    product.setStatus(Constants.STATUS_ACTIVE);
    return productRepository.save(product);
  }

  /**
   *
   * @param requestProduct
   * @throws CommonException
   */
  private void validateRequiredFields(ProductDTO requestProduct) throws CommonException {
    if (requestProduct.getProductName() == null || requestProduct.getProductName().isEmpty()) {
      throw new CommonException(Constants.ERROR_PRODUCT_VALIDATE, "Product name is required.",
          HttpStatus.BAD_REQUEST);
    }

    if (requestProduct.getDescription() == null || requestProduct.getDescription().isEmpty()) {
      throw new CommonException(Constants.ERROR_PRODUCT_VALIDATE, "Description is required.",
          HttpStatus.BAD_REQUEST);
    }

    if (requestProduct.getPrice() == null) {
      throw new CommonException(Constants.ERROR_PRODUCT_VALIDATE, "Price is required.",
          HttpStatus.BAD_REQUEST);
    }

    if (requestProduct.getStockQuantity() == null) {
      throw new CommonException(Constants.ERROR_PRODUCT_VALIDATE, "Stock quantity is required.",
          HttpStatus.BAD_REQUEST);
    }
  }

  /**
   *
   * @param productId
   * @param requestProduct
   * @return
   */
  @Override
  @Transactional
  public Product updateProduct(Long productId, ProductDTO requestProduct) {
    Optional<Product> optionalProduct = productRepository.findById(productId);
    if (optionalProduct.isPresent()) {
      Product product = optionalProduct.get();
      BeanUtils.copyProperties(requestProduct, product);
      return productRepository.save(product);
    } else {
      throw new CommonException(Constants.ERROR_DELETE_PRODUCT_ID_NOT_FOUND, "Product not found",
          HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  @Transactional
  public void deleteProduct(Long productId) {
    Optional<Product> productOptional = productRepository.findById(productId);
    if (productOptional.isPresent()) {
      Product product = productOptional.get();
      product.setStatus(Constants.STATUS_INACTIVE);
      productRepository.save(product);
    } else {
      throw new CommonException(Constants.ERROR_DELETE_PRODUCT_ID_NOT_FOUND, "Product not found",
          HttpStatus.BAD_REQUEST);
    }
  }
}
