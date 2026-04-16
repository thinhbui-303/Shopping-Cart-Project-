package com.ecom.shopping_cart.repository;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.shopping_cart.model.ProductOrder;

public interface OrderRepository extends JpaRepository<ProductOrder,Integer> {
    List<ProductOrder> findByUserId(Integer uid);

    ProductOrder findByOrderId(String orderId);

}
