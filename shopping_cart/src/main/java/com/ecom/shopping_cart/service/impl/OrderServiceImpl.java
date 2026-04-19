package com.ecom.shopping_cart.service.impl;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.shopping_cart.model.Cart;
import com.ecom.shopping_cart.model.OrderAddress;
import com.ecom.shopping_cart.model.OrderRequest;
import com.ecom.shopping_cart.model.ProductOrder;
import com.ecom.shopping_cart.repository.CartRepository;
import com.ecom.shopping_cart.repository.OrderRepository;
import com.ecom.shopping_cart.service.interf.OrderService;
import com.ecom.shopping_cart.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService{


    @Autowired
    OrderRepository orderRepository;

    
    OrderAddress orderAddress;

    @Autowired
    CartRepository cartRepository;

   

    @Override
    @Transactional
    public void saveOrder(Integer uid, OrderRequest orderRequest){
        List<Cart> carts = cartRepository.findCartByUserId(uid);
        for (Cart cart : carts) {
            ProductOrder order = new ProductOrder();
            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(LocalDate.now());
            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());
            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());
            order.setOrderStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            OrderAddress orderAddress = new OrderAddress();

            orderAddress.setFirstName(orderRequest.getFirstName());
            orderAddress.setLastName(orderRequest.getLastName());
            orderAddress.setCity(orderRequest.getCity());
            orderAddress.setPhoneNumber(orderRequest.getPhoneNumber());
            orderAddress.setState(orderRequest.getState());
            orderAddress.setPinCode(orderRequest.getPincode());

            order.setOrderAddress(orderAddress);
            orderRepository.save(order);

        }
        cartRepository.deleteCartByUserId(uid);
        
    }
    @Override
    public List<ProductOrder> getOrderByUserId(Integer uid){
        return orderRepository.findByUserId(uid);
    }
    @Override
    public ProductOrder updateStatusOrder(Integer id , String status){
        Optional<ProductOrder> findbyid  = orderRepository.findById(id);
        if(findbyid.isPresent()){
            ProductOrder productOrder = findbyid.get();
            productOrder.setOrderStatus(status);
            orderRepository.save(productOrder);
            return productOrder;
        }

        return null;
    }
    @Override
    public List<ProductOrder> getAllOrder(){
        return orderRepository.findAll();
    }
    @Override
    public ProductOrder searchByOrderId(String orderId){
        return orderRepository.findByOrderId(orderId);
    }
    @Override
        public Page<ProductOrder> getAllOrderPagination(Integer pageNo, Integer pageSize){
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            return orderRepository.findAll(pageable);
        }


}
