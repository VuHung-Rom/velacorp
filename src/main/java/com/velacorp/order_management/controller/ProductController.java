package com.velacorp.order_management.controller;

import com.velacorp.order_management.common.CommonException;
import com.velacorp.order_management.entity.Product;
import com.velacorp.order_management.entity.dto.BaseResponse;
import com.velacorp.order_management.entity.dto.ProductDTO;
import com.velacorp.order_management.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

  /**
   * api USP05: Như một quản trị viên, tôi muốn có khả năng lấy danh sách tất cả
   * các sản phẩm để xem
   * toàn bộ danh sách sản phẩm có sẵn trong cửa hàng.
   *
   * @return
   */
  @GetMapping("/all")
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

  /**
   * api USP02: Như một quản trị viên, tôi muốn có khả năng xem thông tin chi tiết của một sản phẩm
   * dựa trên ID để kiểm tra các thuộc tính của sản phẩm.
   *
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  public ResponseEntity<BaseResponse> getProductById(@PathVariable Long id) {
    BaseResponse response = new BaseResponse();
    try {
      Optional<Product> products = productService.getProductById(id);
      response.setResponseCode("0");
      response.setMessage("Success");
      response.setData(products);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
      logger.error("An error occurred while getting products by id", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(response);
    }
  }

  /**
   * USP01: Như một quản trị viên, tôi muốn có khả năng thêm một sản phẩm mới vào cửa hàng để cập
   * nhật danh sách sản phẩm.
   *
   * @param requestProduct
   * @return
   */
  @PostMapping("/create")
  public ResponseEntity<BaseResponse> createProduct(@RequestBody ProductDTO requestProduct) {
    BaseResponse response = new BaseResponse();
    try {
      Product createdProduct = productService.createProduct(requestProduct);
      response.setResponseCode("0");
      response.setMessage("Product created successfully");
      response.setData(createdProduct);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (CommonException e) {
      response.setResponseCode(e.getErrorCode());
      response.setMessage(e.getErrorMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    } catch (Exception e) {
      logger.error("An error occurred while creating product", e);
      response.setResponseCode("500");
      response.setMessage("Internal Server Error");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * api USP03: Như một quản trị viên, tôi muốn có khả năng cập nhật thông tin của một sản phẩm để
   * điều chỉnh giá cả hoặc mô tả sản phẩm.
   *
   * @param id
   * @param requestProduct
   * @return
   */
  @PutMapping("/{id}")
  public ResponseEntity<BaseResponse> updateProduct(@PathVariable Long id,
      @RequestBody ProductDTO requestProduct) {
    BaseResponse response = new BaseResponse();
    try {
      Product updatedProduct = productService.updateProduct(id, requestProduct);
      response.setResponseCode("0");
      response.setMessage("Product updated successfully");
      response.setData(updatedProduct);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (CommonException e) {
      response.setResponseCode(e.getErrorCode());
      response.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } catch (Exception e) {
      response.setResponseCode("500");
      response.setMessage("Internal Server Error");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   *USP04: Như một quản trị viên, tôi muốn có khả năng xóa một sản phẩm dựa
   * trên ID để loại bỏ các sản phẩm không còn cần thiết.
   * @param id
   * @return
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<BaseResponse> deleteProduct(@PathVariable Long id) {
    BaseResponse response = new BaseResponse();
    try {
      productService.deleteProduct(id);
      response.setResponseCode("0");
      response.setMessage("Product deleted successfully");
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (CommonException e) {
      response.setResponseCode("404");
      response.setMessage("not found");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } catch (Exception e) {
      response.setResponseCode("500");
      response.setMessage("Internal Server Error");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * api USP06: Như một quản trị viên, tôi muốn có khả năng tìm kiếm sản phẩm dựa trên tên hoặc
   * mô tả để dễ dàng tìm kiếm sản phẩm cụ thể.
   * @param keyword
   * @return
   */
  @GetMapping("/search")
  public ResponseEntity<BaseResponse> searchProducts(@RequestParam(required = false) String keyword) {
    BaseResponse response = new BaseResponse();
    try {
      List<Product> products = productService.searchProducts(keyword);
      response.setResponseCode("0");
      response.setMessage("Products found successfully");
      response.setData(products);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
      response.setResponseCode("500");
      response.setMessage("Internal Server Error");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }
}
