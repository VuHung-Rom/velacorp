package com.velacorp.order_management.entity.dto;

import com.velacorp.order_management.entity.Product;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
  private List<Product> products;
  private Long totalCount;

  public ProductResponse(List<Product> products, Long totalCount) {
    this.products = products;
    this.totalCount = totalCount;
  }

}
