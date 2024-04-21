package com.velacorp.order_management.controller;

import com.velacorp.order_management.common.CommonException;
import com.velacorp.order_management.entity.OrderDetail;
import com.velacorp.order_management.entity.Orders;
import com.velacorp.order_management.entity.dto.BaseResponse;
import com.velacorp.order_management.entity.dto.OrderDTO;
import com.velacorp.order_management.entity.dto.OrdersResponse;
import com.velacorp.order_management.service.OrderService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
  @Autowired
  private OrderService orderService;

  private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

  /**
   * api UUOD04: Như một quản trị viên, tôi muốn có khả năng lấy danh sách tất cả các đơn hàng để
   * quản lý tình trạng của các đơn hàng.
   *
   * @return
   */
  @GetMapping("/all")
  public ResponseEntity<BaseResponse> getAllOrders() {
    BaseResponse response = new BaseResponse();
    try {
      List<Orders> orders = orderService.getAllOrders();
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

  /**
   * API UOD05: Như một quản trị viên, tôi muốn có khả năng tìm kiếm đơn hàng dựa trên tên khách
   * hàng hoặc ID đơn hàng để dễ dàng tìm kiếm đơn hàng cụ thể.
   *
   * @param id
   * @param keyword
   * @return
   */
  @GetMapping("/search")
  public ResponseEntity<BaseResponse> searchOrders(@RequestParam(required = false) Long id,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) Integer pageSize,
      @RequestParam(required = false) Integer pageNumber) {
    if (pageSize == null ) {
      pageSize = 20;
    }
    if (pageNumber == null ) {
      pageNumber = 0;
    }
    BaseResponse response = new BaseResponse();
    try {
      if (id == null && keyword == null) {
        response.setResponseCode("400");
        response.setMessage("At least one of id or keyword must be provided.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }
      OrdersResponse ordersResponse = orderService.searchOrders(id, keyword, pageSize, pageNumber);
      response.setResponseCode("0");
      response.setMessage("Orders retrieved successfully");
      response.setData(ordersResponse);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
      response.setResponseCode("500");
      response.setMessage("Internal Server Error");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }


  /**
   * api UOD01: Như một quản trị viên, tôi muốn có khả năng tạo đơn hàng mới với thông tin cá nhân
   * và danh sách sản phẩm để hỗ trợ khách hàng đặt hàng.
   *
   * @param orderDTO
   * @return
   */
  @PostMapping("/create")
  public ResponseEntity<BaseResponse> createOrder(@RequestBody OrderDTO orderDTO) {
    BaseResponse response = new BaseResponse();
    try {
      Orders createdOrder = orderService.createOrders(orderDTO);
      response.setResponseCode("0");
      response.setMessage("Order created successfully");
      response.setData(createdOrder);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (CommonException ex) {
      response.setResponseCode("400");
      response.setMessage(ex.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    } catch (Exception ex) {
      response.setResponseCode("500");
      response.setMessage("Internal Server Error");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * Như một quản trị viên, tôi muốn có khả năng cập nhật thông tin của một đơn hàng để phục vụ yêu
   * cầu vận hành và yêu cầu của khách hàng.
   *
   * @param orderId
   * @param requestOrder
   * @return
   */
  @PutMapping("/{orderId}")
  public ResponseEntity<BaseResponse> updateOrder(@PathVariable Long orderId,
      @RequestBody OrderDTO requestOrder) {
    BaseResponse response = new BaseResponse();
    try {
      Orders updatedOrder = orderService.updateOrder(orderId, requestOrder);
      response.setResponseCode("0");
      response.setMessage("Order updated successfully");
      response.setData(updatedOrder);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (CommonException e) {
      response.setResponseCode("404");
      response.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } catch (Exception e) {
      response.setResponseCode("500");
      response.setMessage("Internal Server Error");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * UOD02: Như một quản trị viên, tôi muốn có khả năng xem thông tin chi tiết của một đơn hàng dựa
   * trên ID để kiểm tra tình trạng đơn hàng.
   *
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  public ResponseEntity<BaseResponse> getOrderDetailById(@PathVariable Long id) {
    BaseResponse response = new BaseResponse();
    try {
      Optional<OrderDetail> orderDetail = orderService.getOrderDetailById(id);
      response.setResponseCode("0");
      response.setMessage("Success");
      response.setData(orderDetail);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
      logger.error("An error occurred while getting order detail by id", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }
}




