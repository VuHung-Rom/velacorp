package com.velacorp.order_management.service.impl;

import com.velacorp.order_management.common.CommonException;
import com.velacorp.order_management.entity.OrderDetail;
import com.velacorp.order_management.entity.Orders;
import com.velacorp.order_management.entity.dto.BaseResponse;
import com.velacorp.order_management.entity.dto.OrderDTO;
import com.velacorp.order_management.repository.OrderDetailRepository;
import com.velacorp.order_management.repository.OrderRepository;
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
  public List<Orders> searchOrders(Long id, String keyword) throws Exception {
    BaseResponse response = new BaseResponse();
    try {
      List<Orders> orders = orderRepository.findByOrderIdOrCustomerNameContainingIgnoreCase(id,
          keyword);
      return orders;
    } catch (Exception exception) {
      response.setResponseCode("ERR_SEARCH_ORDER_FAIL");
      response.setMessage("Cannot retrieve order");
      throw new CommonException(exception, "ERR_SEARCH_ORDER_FAIL", "Cannot retrieve order");
    }
  }

  @Override
  public Orders createOrders(OrderDTO orderDTO) {
    validateOrderDTO(orderDTO);
    Orders order = new Orders();
    BeanUtils.copyProperties(orderDTO, order);
    order.setStatus("1");
    return orderRepository.save(order);
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

  }

  private boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  @Override
  public Orders updateOrder(Long orderId, OrderDTO requestOrder) {
    Optional<Orders> optionalOrder = orderRepository.findById(orderId);
    if (optionalOrder.isPresent()) {
      Orders existingOrder = optionalOrder.get();
      BeanUtils.copyProperties(requestOrder, existingOrder);
      return orderRepository.save(existingOrder);
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
