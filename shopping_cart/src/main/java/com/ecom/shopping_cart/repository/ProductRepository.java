package com.ecom.shopping_cart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.shopping_cart.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    public List<Product> findByIsActiveTrue();

    public List<Product> findByCategory(String category);

    public List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch1, String ch2);

    public Page<Product> findByIsActiveTrue(Pageable pageable);

    public Page<Product> findByCategory(Pageable pageable, String category);

   public Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch1, String ch2, Pageable pageable);

   public Page<Product> findByIsActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch1 , String ch2, Pageable pageable);

}
