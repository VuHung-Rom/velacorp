package com.velacorp.order_management.entity.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderProductsDTO {
  private Long productId;
  private Long quantity;

}
