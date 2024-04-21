package com.velacorp.order_management.service.impl;

import com.velacorp.order_management.common.CommonException;
import com.velacorp.order_management.common.Utils;
import com.velacorp.order_management.common.Utils.Constants;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private OrderDetailRepository orderDetailRepository;
  @Autowired
  private ProductRepository productRepository;
  @Override
  public List<Orders> getAllOrders() {
    BaseResponse response = new BaseResponse();
      List<Orders> orders = orderRepository.findAll();
      return orders;
  }

  @Override
  public OrdersResponse searchOrders(Long id, String keyword, int pageSize, int pageNumber)
      throws Exception {
    int offset = pageNumber * pageSize;
    List<Orders> orders = orderRepository.findByOrderIdOrCustomerNameContainingIgnoreCase(id,
        keyword, pageSize, offset);
    Long totalCount = orderRepository.findByOrderIdOrCustomerNameContainingIgnoreCaseCount(id,
        keyword);
    return new OrdersResponse(orders, totalCount);
  }


  @Override
  @Transactional
  public Orders createOrders(OrderDTO orderDTO) {
    validateOrderDTO(orderDTO);
    String lstProductIdError = "";
    String lstProductIdNotEnough = "";
    Double totalPrice = 0D;
    for (OrderProductsDTO orderProductsDTO : orderDTO.getLstOrderProduct()) {
      Optional<Product> productOptional = productRepository.findById(
          orderProductsDTO.getProductId());
      if (!productOptional.isPresent() || !Constants.STATUS_ACTIVE.equals(
          productOptional.get().getStatus())) {
        lstProductIdError = ";" + orderProductsDTO.getProductId();
        continue;
      }
      Product product = productOptional.get();
      //Process quantity of product
      if (product.getStockQuantity() < orderProductsDTO.getQuantity()) {
        lstProductIdNotEnough = ";" + orderProductsDTO.getProductId();
        continue;
      } else {
        product.setStockQuantity(product.getStockQuantity() - orderProductsDTO.getQuantity());
      }
      productRepository.save(product);
      totalPrice += product.getPrice() * orderProductsDTO.getQuantity();
    }
    if (!lstProductIdError.isEmpty()) {
      throw new CommonException("PRODUCT_NOT_FOUND",
          "ProductId not found in database : " + lstProductIdError, HttpStatus.BAD_REQUEST);
    }
    if (!lstProductIdNotEnough.isEmpty()) {
      throw new CommonException("PRODUCT_QUANTITY_NOT_ENOUGH",
          "ProductId quantity are not enough : " + lstProductIdNotEnough, HttpStatus.BAD_REQUEST);
    }
    Orders order = new Orders();
    BeanUtils.copyProperties(orderDTO, order);
    order.setTotalAmount(totalPrice);
    order.setOrderDate(new Date());
    order.setStatus(Constants.STATUS_ACTIVE);
    orderRepository.save(order);
    for (OrderProductsDTO orderProductsDTO : orderDTO.getLstOrderProduct()) {
      OrderDetail orderDetail = new OrderDetail();
      Optional<Product> productOptional = productRepository.findById(
          orderProductsDTO.getProductId());

      orderDetail.setProduct(productOptional.get());
      orderDetail.setQuantity(orderProductsDTO.getQuantity());
      orderDetail.setOrder(order);
      orderDetail.setStatus(Constants.STATUS_ACTIVE);
    }
    return order;
  }

  private boolean isValidPhoneNumber(String phoneNumber) {
    String regex = "^\\d{10}$";
    return phoneNumber.matches(regex);
  }

  private void validateOrderDTO(OrderDTO orderDTO) {

    if (isNullOrEmpty(orderDTO.getCustomerName())) {
      throw new CommonException("VALIDATION_ERROR", "Customer name is required",
          HttpStatus.BAD_REQUEST);
    }

    if (isNullOrEmpty(orderDTO.getAddress())) {
      throw new CommonException("VALIDATION_ERROR", "Address is required", HttpStatus.BAD_REQUEST);
    }

    if (isNullOrEmpty(orderDTO.getEmail())) {
      throw new CommonException("VALIDATION_ERROR", "Email is required", HttpStatus.BAD_REQUEST);
    }
    if (!isValidPhoneNumber(orderDTO.getPhoneNumber())) {
      throw new CommonException("VALIDATION_ERROR", "Invalid PhoneNumber format",
          HttpStatus.BAD_REQUEST);
    }
    if (orderDTO.getLstOrderProduct() == null || orderDTO.getLstOrderProduct().isEmpty()) {
      throw new CommonException("VALIDATION_ERROR", "list order product is required",
          HttpStatus.BAD_REQUEST);
    }
  }

  private boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  @Override
  @Transactional
  public Orders updateOrder(Long orderId, OrderDTO requestOrder) throws CommonException {
    validateOrderDTO(requestOrder);
    Optional<Orders> optionalOrder = orderRepository.findById(orderId);
    if (optionalOrder.isPresent()) {
      Orders existingOrder = optionalOrder.get();
      List<OrderDetail> existingOrderDetails = orderDetailRepository.findByOrderAndStatusEquals(
          existingOrder, "1");

      Double totalPrice = 0D;
      String lstProductIdError = "";
      String lstProductIdNotEnough = "";

      for (OrderProductsDTO orderProductsDTO : requestOrder.getLstOrderProduct()) {
        Long productId = orderProductsDTO.getProductId();
        Optional<OrderDetail> existingDetailOptional = existingOrderDetails.stream()
            .filter(detail -> detail.getProduct().getId().equals(productId))
            .findFirst();

        OrderDetail orderDetail;
        if (existingDetailOptional.isPresent()) {
          Optional<Product> productOptional = productRepository.findById(productId);
          if (!productOptional.isPresent() || !Constants.STATUS_ACTIVE.equals(
              productOptional.get().getStatus())) {
            lstProductIdError = ";" + orderProductsDTO.getProductId();
            continue;
          }
          Product product = productOptional.get();
          totalPrice += product.getPrice() * orderProductsDTO.getQuantity();
          //update quantity if existed in DB
          orderDetail = existingDetailOptional.get();
          Long oldQuantity = orderDetail.getQuantity();
          orderDetail.setQuantity(orderProductsDTO.getQuantity());
          //Process quantity of product
          if (product.getStockQuantity() < orderProductsDTO.getQuantity()) {
            lstProductIdNotEnough = ";" + orderProductsDTO.getProductId();
            continue;
          } else {
            product.setStockQuantity(
                product.getStockQuantity() - orderProductsDTO.getQuantity() + oldQuantity);
          }
        } else {
          //Create new  Order detail if existed in DB
          orderDetail = new OrderDetail();
          Optional<Product> productOptional = productRepository.findById(productId);
          if (!productOptional.isPresent() || !Constants.STATUS_ACTIVE.equals(
              productOptional.get().getStatus())) {
            lstProductIdError = ";" + orderProductsDTO.getProductId();
            continue;
          }
          Product product = productOptional.get();
          //Process quantity of product
          if (product.getStockQuantity() < orderProductsDTO.getQuantity()) {
            lstProductIdNotEnough = ";" + orderProductsDTO.getProductId();
            continue;
          } else {
            product.setStockQuantity(product.getStockQuantity() - orderProductsDTO.getQuantity());
          }
          orderDetail.setProduct(product);
          orderDetail.setQuantity(orderProductsDTO.getQuantity());
          orderDetail.setUnitPrice(product.getPrice());
          orderDetail.setOrder(existingOrder);
          orderDetail.setStatus(Constants.STATUS_ACTIVE);
          totalPrice += productOptional.get().getPrice() * orderProductsDTO.getQuantity();
        }

        orderDetailRepository.save(orderDetail);
      }

      if (!lstProductIdError.isEmpty()) {
        throw new CommonException("PRODUCT_NOT_FOUND",
            "ProductId not found in database: " + lstProductIdError, HttpStatus.BAD_REQUEST);
      }

      if (!lstProductIdNotEnough.isEmpty()) {
        throw new CommonException("PRODUCT_QUANTITY_NOT_ENOUGH",
            "ProductId quantity are not enough : " + lstProductIdNotEnough, HttpStatus.BAD_REQUEST);
      }
      //recalculate total amount
      existingOrder.setTotalAmount(totalPrice);

      //remove product not send in request
      existingOrderDetails.removeIf(detail ->
          requestOrder.getLstOrderProduct().stream()
              .anyMatch(p -> p.getProductId().equals(detail.getProduct().getId())));

      for (OrderDetail orderDetail : existingOrderDetails) {
        orderDetail.setStatus(Constants.STATUS_INACTIVE);
        orderDetailRepository.save(orderDetail);

        //Set quantity in product table
        Product product = orderDetail.getProduct();
        if (product == null) {
//          throw new CommonException("CAN_NOT_RETURN_PRODUCT",
//              "Cannot return product because cannot find productId: " +  , HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
          product.setStockQuantity(product.getStockQuantity() + orderDetail.getQuantity());
        }
      }

      existingOrder = orderRepository.save(existingOrder);
      return existingOrder;
    } else {
      throw new CommonException(Utils.Constants.ERROR_NOT_FOUND,
          "Order with " + orderId + "id not found",
          HttpStatus.NOT_FOUND);
    }
  }


  @Override
  public Optional<OrderDetail> getOrderDetailById(Long id) throws Exception {
    BaseResponse response = new BaseResponse();
      Optional<OrderDetail> orderDetail = orderDetailRepository.findById(id);
      return orderDetail;
  }
}
