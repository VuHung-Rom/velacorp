package com.velacorp.order_management.entity.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDTO {
  private Date orderDate;
  private String customerName;
  private String address;
  private String email;
  private String phoneNumber;
  private Double totalAmount;
}
