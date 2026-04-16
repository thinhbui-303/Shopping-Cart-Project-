package com.ecom.shopping_cart.service.interf;

import java.util.List;

import com.ecom.shopping_cart.model.Cart;

public interface CartService {
    public Cart saveCart(Integer pid ,Integer uid);

    public Integer getCountCart(Integer userid);

    public List<Cart> getCartByUserId(Integer uid);
    public double getTotalOrderPrice(Integer uid);

    public void updateQuantityCart(String sy, Integer cid);
}
