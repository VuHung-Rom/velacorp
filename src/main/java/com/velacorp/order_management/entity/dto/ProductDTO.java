package com.velacorp.order_management.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDTO {
  private Long id;
  private String productName;
  private String description;
  private Double price;
  private Long stockQuantity;

}
