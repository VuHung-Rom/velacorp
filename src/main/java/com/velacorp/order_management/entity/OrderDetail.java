package com.velacorp.order_management.entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ORDERDETAIL")
public class OrderDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "ORDERID", nullable = false)
  private Order order;
  @ManyToOne
  @JoinColumn(name = "PRODUCTID", nullable = false)
  private Product product;
  private Long quantity;
  private Double unitPrice;
}
