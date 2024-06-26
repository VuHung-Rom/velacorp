package com.velacorp.order_management.controller;

import com.velacorp.order_management.common.CommonException;
import com.velacorp.order_management.common.Utils;
import com.velacorp.order_management.common.Utils.Constants;
import com.velacorp.order_management.entity.Product;
import com.velacorp.order_management.entity.dto.BaseResponse;
import com.velacorp.order_management.entity.dto.ProductDTO;
import com.velacorp.order_management.entity.dto.ProductResponse;
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
    logger.info("BEGIN getAllProducts. Request={}");
    BaseResponse response = new BaseResponse();
    try {
      List<Product> products = productService.getAllProducts();
      response.setResponseCode(Constants.SUCCESS);
      response.setMessage("Success");
      response.setData(products);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (CommonException e) {
      if(e.getHttpCode()!= null){
        response.setResponseCode(e.getErrorCode());
        response.setMessage(e.getErrorMessage());
        return ResponseEntity.status(e.getHttpCode()).body(response);
      }else {
        response.setResponseCode(e.getErrorCode());
        response.setMessage(e.getErrorMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
      }
    } catch (Exception e) {
      logger.error("An error occurred while getting all products", e);
      response.setResponseCode(Constants.ERROR_UNKNOW);
      response.setMessage("Unknow error when get product");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(response);
    }finally {
      logger.info("END getAllProducts, response=" + Utils.objectToJson(response));
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
    logger.info("BEGIN getProductById. Request={"+id+"}");
    BaseResponse response = new BaseResponse();
    try {
      Optional<Product> products = productService.getProductById(id);
      if (products.isPresent()) {
        response.setResponseCode(Constants.SUCCESS);
        response.setMessage("Success");
        response.setData(products.get());
        return ResponseEntity.status(HttpStatus.OK).body(response);
      } else {
        response.setResponseCode(Constants.ERROR_NOT_FOUND);
        response.setMessage("Product not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }
    } catch (CommonException e) {
      if(e.getHttpCode()!= null){
        response.setResponseCode(e.getErrorCode());
        response.setMessage(e.getErrorMessage());
        return ResponseEntity.status(e.getHttpCode()).body(response);
      }else {
        response.setResponseCode(e.getErrorCode());
        response.setMessage(e.getErrorMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
      }
    }  catch (Exception e) {
      logger.error("An error occurred while getting products by id", e);
      response.setResponseCode(Constants.ERROR_UNKNOW);
      response.setMessage("Unknown error when get product");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(response);
    }finally {
      logger.info("END getProductById, response=" + Utils.objectToJson(response));
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
    logger.info("BEGIN createProduct. Request={"+Utils.objectToJson(requestProduct)+"}");
    try {
      Product createdProduct = productService.createProduct(requestProduct);
      response.setResponseCode(Constants.SUCCESS);
      response.setMessage("Product created successfully");
      response.setData(createdProduct);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (CommonException e) {
      if(e.getHttpCode()!= null){
        response.setResponseCode(e.getErrorCode());
        response.setMessage(e.getErrorMessage());
        return ResponseEntity.status(e.getHttpCode()).body(response);
      }else {
        response.setResponseCode(e.getErrorCode());
        response.setMessage(e.getErrorMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
      }
    } catch (Exception e) {
      logger.error("An error occurred while creating product", e);
      response.setResponseCode(Constants.ERROR_UNKNOW);
      response.setMessage("Unknown error when get product");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(response);
    }finally {
      logger.info("END createProduct, response=" + Utils.objectToJson(response));
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
    logger.info("BEGIN updateProduct.  Request={requestProduct :"+Utils.objectToJson(requestProduct) +";id:" +id+"}");
    BaseResponse response = new BaseResponse();
    try {
      Product updatedProduct = productService.updateProduct(id, requestProduct);
      response.setResponseCode(Constants.SUCCESS);
      response.setMessage("Product updated successfully");
      response.setData(updatedProduct);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }  catch (CommonException e) {
      if(e.getHttpCode()!= null){
        response.setResponseCode(e.getErrorCode());
        response.setMessage(e.getErrorMessage());
        return ResponseEntity.status(e.getHttpCode()).body(response);
      }else {
        response.setResponseCode(e.getErrorCode());
        response.setMessage(e.getErrorMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
      }
    }  catch (Exception e) {
      response.setResponseCode(Constants.ERROR_UNKNOW);
      response.setMessage("Unknown error when get product");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(response);
    }finally {
      logger.info("END updateProduct, response=" + Utils.objectToJson(response));
    }
  }

  /**
   *USP04: Như một quản trị viên, tôi muốn có khả năng xóa một sản phẩm dựa
   * trên ID để loại bỏ các sản phẩm không còn cần thiết.
   * @param productId
   * @return
   */
  @DeleteMapping("/{productId}")
  public ResponseEntity<BaseResponse> deleteProduct(@PathVariable Long productId) {
    BaseResponse response = new BaseResponse();
    logger.info("BEGIN deleteProduct.  Request={productId :" + productId + "}");
    try {
      productService.deleteProduct(productId);
      response.setResponseCode(Constants.SUCCESS);
      response.setMessage("Product deleted successfully");
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (CommonException e) {
      response.setResponseCode(e.getErrorCode());
      response.setMessage(e.getErrorMessage());
      if (e.getHttpCode() != null) {
        return new ResponseEntity<>(response, e.getHttpCode());
      } else {
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      response.setResponseCode(Constants.ERROR_UNKNOW);
      response.setMessage("Unknown error when get product");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(response);
    } finally {
      logger.info("END deleteProduct, response=" + Utils.objectToJson(response));
    }
  }

  /**
   * api USP06: Như một quản trị viên, tôi muốn có khả năng tìm kiếm sản phẩm dựa trên tên hoặc
   * mô tả để dễ dàng tìm kiếm sản phẩm cụ thể.
   * @param keyword
   * @return
   */
  @GetMapping("/search")
  public ResponseEntity<BaseResponse> searchProducts(@RequestParam(required = false) String keyword,
    @RequestParam(required = false) Integer pageSize,
      @RequestParam(required = false) Integer pageNumber) {
    logger.info("BEGIN searchProducts, request={", ";keyword:" + keyword
        + ";pageSize" + pageSize + ";pageNumber=" + pageNumber + "}");

    if (pageSize == null ) {
      pageSize = 20;
    }
    if (pageNumber == null ) {
      pageNumber = 0;
    }
    BaseResponse response = new BaseResponse();
    try {
      ProductResponse products = productService.searchProducts(keyword,  pageSize,  pageNumber);
      response.setResponseCode("0");
      response.setMessage("Products found successfully");
      response.setData(products);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }catch (CommonException e) {
      response.setResponseCode(e.getErrorCode());
      response.setMessage(e.getErrorMessage());
      if (e.getHttpCode() != null) {
        return new ResponseEntity<>(response, e.getHttpCode());
      } else {
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }
    }  catch (Exception e) {
      response.setResponseCode(Constants.ERROR_UNKNOW);
      response.setMessage("Unknown error when get product");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(response);
    }finally {
      logger.info("END searchProducts, response=" + Utils.objectToJson(response));
    }
  }
}
