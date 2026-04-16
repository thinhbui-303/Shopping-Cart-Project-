package com.ecom.shopping_cart.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom.shopping_cart.model.Cart;
import com.ecom.shopping_cart.model.Product;
import com.ecom.shopping_cart.model.UserDtls;
import com.ecom.shopping_cart.repository.CartRepository;
import com.ecom.shopping_cart.repository.ProductRepository;
import com.ecom.shopping_cart.repository.UserRepository;
import com.ecom.shopping_cart.service.interf.CartService;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public Cart saveCart(Integer pid , Integer uid){
        UserDtls user = userRepository.findById(uid).get();
        Product product = productRepository.findById(pid).get();
        Cart cartStatus = cartRepository.findByProductIdAndUserId(pid, uid);
        Cart cart = null;
        if(ObjectUtils.isEmpty(cartStatus)){
            cart =new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(1);
            cart.setTotalPrice(product.getDiscountPrice());
            
        } 
        else{
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity()+1);
            cart.setTotalPrice(cart.getQuantity()*cart.getProduct().getDiscountPrice());
        
        }
        Cart saveCart = cartRepository.save(cart);
        return saveCart ;
    }
    @Override
    public Integer getCountCart(Integer userid){
        return cartRepository.countCartByUserId(userid);
    }
    @Override
    public List<Cart> getCartByUserId(Integer uid){
        return cartRepository.findCartByUserId(uid);
    }
    @Override
    public double getTotalOrderPrice(Integer uid){
        List<Cart> carts = cartRepository.findCartByUserId(uid);
        double totalOrderPrice = 0;
        for (Cart cart : carts) {
            totalOrderPrice += cart.getTotalPrice();
            
        }
        return totalOrderPrice;
    }
    @Override
    public void updateQuantityCart(String sy, Integer cid){
        Cart cart = cartRepository.findById(cid).get(); 
        Integer quantityProduct;
        if(sy.equalsIgnoreCase("de")){
            
            if(cart.getQuantity() <= 1){
                cartRepository.delete(cart);
                return;
            }
            quantityProduct = cart.getQuantity()-1;
        }
        else{
             quantityProduct = cart.getQuantity()+1;
        }
        cart.setQuantity(quantityProduct);
        Double price = quantityProduct * cart.getProduct().getDiscountPrice();
        cart.setTotalPrice(price);
        cartRepository.save(cart);
        
    }

}
