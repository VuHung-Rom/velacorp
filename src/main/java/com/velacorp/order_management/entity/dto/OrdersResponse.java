package com.velacorp.order_management.entity.dto;

import com.velacorp.order_management.entity.Orders;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdersResponse {
  private List<Orders> orders;
  private Long totalCount;

  public OrdersResponse(List<Orders> orders, Long totalCount) {
    this.orders = orders;
    this.totalCount = totalCount;
  }

}
