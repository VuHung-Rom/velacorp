package com.velacorp.order_management.repository;

import com.velacorp.order_management.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query("SELECT p FROM Product p WHERE p.status = '1' AND (p.productName LIKE %?1% OR p.description LIKE %?1%)")
  List<Product> searchProducts(String keyword, int pageSize, int offset);
  @Query("SELECT COUNT(p) FROM Product p WHERE p.status ='1' AND (p.productName LIKE %?1% OR p.description LIKE %?1%)")
  long countSearchResults(String keyword);
}

