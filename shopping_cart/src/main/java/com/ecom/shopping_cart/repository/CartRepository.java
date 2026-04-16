package com.ecom.shopping_cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.shopping_cart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{
    public Cart findByProductIdAndUserId(Integer pid, Integer uid);

    public Integer countCartByUserId(Integer userid);

    public List<Cart> findCartByUserId(Integer uid);
}
