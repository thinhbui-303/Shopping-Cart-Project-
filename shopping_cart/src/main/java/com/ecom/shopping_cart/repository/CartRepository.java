package com.ecom.shopping_cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ecom.shopping_cart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{
    public Cart findByProductIdAndUserId(Integer pid, Integer uid);

    public Integer countCartByUserId(Integer userid);

    public List<Cart> findCartByUserId(Integer uid);

    @Modifying
    @Query("delete from Cart c where c.user.id = ?1")
    public void deleteCartByUserId(Integer uid);
}
