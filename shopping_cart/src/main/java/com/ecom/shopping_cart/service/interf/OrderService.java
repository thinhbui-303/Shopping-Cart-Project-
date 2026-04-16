package com.ecom.shopping_cart.service.interf;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecom.shopping_cart.model.OrderRequest;
import com.ecom.shopping_cart.model.ProductOrder;

public interface OrderService {
    
    public void saveOrder(Integer uid, OrderRequest ordeRequest)throws Exception;

    public List<ProductOrder> getOrderByUserId(Integer uid);

    public ProductOrder updateStatusOrder(Integer id , String status);

    public List<ProductOrder> getAllOrder();

    public ProductOrder searchByOrderId(String orderId);

    public Page<ProductOrder> getAllOrderPagination(Integer pageNo, Integer pageSize);
}
