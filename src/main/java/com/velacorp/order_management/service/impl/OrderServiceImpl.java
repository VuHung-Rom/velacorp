package com.velacorp.order_management.service.impl;

import com.velacorp.order_management.common.CommonException;
import com.velacorp.order_management.entity.OrderDetail;
import com.velacorp.order_management.entity.Orders;
import com.velacorp.order_management.entity.Product;
import com.velacorp.order_management.entity.dto.BaseResponse;
import com.velacorp.order_management.entity.dto.OrderDTO;
import com.velacorp.order_management.entity.dto.OrderProductsDTO;
import com.velacorp.order_management.entity.dto.OrdersResponse;
import com.velacorp.order_management.repository.OrderDetailRepository;
import com.velacorp.order_management.repository.OrderRepository;
import com.velacorp.order_management.repository.ProductRepository;
import com.velacorp.order_management.service.OrderService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private OrderDetailRepository orderDetailRepository;
  @Autowired
  private ProductRepository productRepository;
  @Override
  public List<Orders> getAllOrders() throws Exception {
    BaseResponse response = new BaseResponse();
    try {
      List<Orders> orders = orderRepository.findAll();
      return orders;
    } catch (Exception exception) {
      response.setResponseCode("ERR_GET_ALL_ORDER_FAIL");
      response.setMessage("Cannot retrieve order");
      throw new CommonException(exception, "ERR_GET_ALL_ORDER_FAIL", "Cannot retrieve order");
    }
  }

  @Override
  public OrdersResponse searchOrders(Long id, String keyword, int pageSize, int pageNumber) throws Exception {
    try {
      int offset = pageNumber  * pageSize;
      List<Orders> orders = orderRepository.findByOrderIdOrCustomerNameContainingIgnoreCase(id, keyword, pageSize, offset);
      Long totalCount = orderRepository.findByOrderIdOrCustomerNameContainingIgnoreCaseCount(id, keyword);
      return new OrdersResponse(orders, totalCount);
    } catch (Exception exception) {
      throw new CommonException(exception, "ERR_SEARCH_ORDER_FAIL", "Cannot retrieve order");
    }
  }


  @Override
  public Orders createOrders(OrderDTO orderDTO) {
    validateOrderDTO(orderDTO);
    String lstProductIdError = "";
    Double totalPrice = 0D;
    for (OrderProductsDTO orderProductsDTO : orderDTO.getLstOrderProduct()) {
      Optional<Product> productOptional = productRepository.findById(
          orderProductsDTO.getProductId());
      if (!productOptional.isPresent()) {
        lstProductIdError = ";" + orderProductsDTO.getProductId();
        continue;
      }
      totalPrice += productOptional.get().getPrice() * orderProductsDTO.getQuantity();
    }
    if (!lstProductIdError.isEmpty()) {
      throw new CommonException("PRODUCT_NOT_FOUND",
          "ProductId not found in database: " + lstProductIdError);
    }
    Orders order = new Orders();
    BeanUtils.copyProperties(orderDTO, order);
    order.setTotalAmount(totalPrice);
    order.setStatus("1");
    orderRepository.save(order);
    for (OrderProductsDTO orderProductsDTO : orderDTO.getLstOrderProduct()) {
      OrderDetail orderDetail = new OrderDetail();
      Optional<Product> productOptional = productRepository.findById(
          orderProductsDTO.getProductId());

      orderDetail.setProduct(productOptional.get());
      orderDetail.setQuantity(orderProductsDTO.getQuantity());
      orderDetail.setOrder(order);
      orderDetail.setStatus("1");
    }
    return order;
  }

  private boolean isValidPhoneNumber(String phoneNumber) {
    String regex = "^\\d{10}$";
    return phoneNumber.matches(regex);
  }

  private void validateOrderDTO(OrderDTO orderDTO) {

    if (isNullOrEmpty(orderDTO.getCustomerName())) {
      throw new CommonException("VALIDATION_ERROR", "Customer name is required");
    }

    if (isNullOrEmpty(orderDTO.getAddress())) {
      throw new CommonException("VALIDATION_ERROR", "Address is required");
    }

    if (isNullOrEmpty(orderDTO.getEmail())) {
      throw new CommonException("VALIDATION_ERROR", "Email is required");
    }
    if (!isValidPhoneNumber(orderDTO.getPhoneNumber())) {
      throw new CommonException("2", "Invalid PhoneNumber format");
    }
    if (orderDTO.getLstOrderProduct() == null || orderDTO.getLstOrderProduct().isEmpty()) {
      throw new CommonException("VALIDATION_ERROR", "list order product is required");
    }
  }

  private boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  @Override
  public Orders updateOrder(Long orderId, OrderDTO requestOrder) {
    validateOrderDTO(requestOrder);
    Optional<Orders> optionalOrder = orderRepository.findById(orderId);
    if (optionalOrder.isPresent()) {
      Orders existingOrder = optionalOrder.get();
      List<OrderDetail> existingOrderDetails = orderDetailRepository.findByOrder(existingOrder);

      Double totalPrice = 0D;
      for (OrderProductsDTO orderProductsDTO : requestOrder.getLstOrderProduct()) {
        Long productId = orderProductsDTO.getProductId();
        Optional<OrderDetail> existingDetailOptional = existingOrderDetails.stream()
            .filter(detail -> detail.getProduct().getId().equals(productId))
            .findFirst();

        OrderDetail orderDetail;
        if (existingDetailOptional.isPresent()) {
          orderDetail = existingDetailOptional.get();
          orderDetail.setQuantity(orderProductsDTO.getQuantity());
        } else {
          orderDetail = new OrderDetail();
          Optional<Product> productOptional = productRepository.findById(productId);
          if (!productOptional.isPresent()) {
            continue;
          }
          Product product = productOptional.get();
          orderDetail.setProduct(product);
          orderDetail.setQuantity(orderProductsDTO.getQuantity());
          orderDetail.setUnitPrice(product.getPrice());
          orderDetail.setOrder(existingOrder);
          orderDetail.setStatus("1");
        }

        orderDetailRepository.save(orderDetail);
      }

      existingOrderDetails.removeIf(detail ->
          requestOrder.getLstOrderProduct().stream()
              .noneMatch(p -> p.getProductId().equals(detail.getProduct().getId())));

      for (OrderDetail orderDetail : existingOrderDetails) {
        orderDetail.setStatus("-1");
        orderDetailRepository.save(orderDetail);
      }

      for (OrderDetail orderDetail : existingOrderDetails) {
        totalPrice += orderDetail.getUnitPrice() * orderDetail.getQuantity();
      }
      existingOrder.setTotalAmount(totalPrice);

      existingOrder = orderRepository.save(existingOrder);
      return existingOrder;
    } else {
      throw new CommonException("Order with id ", " not found");
    }
  }


  @Override
  public Optional<OrderDetail> getOrderDetailById(Long id) throws Exception {
    BaseResponse response = new BaseResponse();
    try {
      Optional<OrderDetail> orderDetail = orderDetailRepository.findById(id);
      return orderDetail;
    } catch (Exception exception) {
      response.setResponseCode("ERR_GET_ORDER_DETAIL_BY_ID_FAIL");
      response.setMessage("Cannot retrieve order detail");
      throw new CommonException(exception, "ERR_GET_ORDER_DETAIL_BY_ID_FAIL", "Cannot retrieve order detail");
    }
  }
}
