package com.ecom.shopping_cart.service.interf;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.shopping_cart.model.Product;

@Service
public interface ProductService {
    public Product saveProduct(Product product);
    
    public List<Product> getAllProducts();

    public Boolean deleteProductById(Integer id);

    public Product getProductById(Integer id);

    public Product updateProduct(Product product ,  MultipartFile file);

    public List<Product> getAllIsActiveProduct(String category);

    public List<Product> searchProduct(String ch);

    public Page<Product> getAllIsActiveProductPagination(Integer pageNo, Integer pageSize, String category);

    public Page<Product> getAllProductPagination(Integer pageNo, Integer pageSize);

    public Page<Product> searchProductPagination(String ch , Integer pageNo , Integer pageSize);

    public Page<Product> searchIsActiveProductPagination(String ch , Integer pageNo, Integer PageSize, String category);
}
