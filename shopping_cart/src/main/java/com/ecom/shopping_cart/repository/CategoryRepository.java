package com.ecom.shopping_cart.repository;

import com.ecom.shopping_cart.model.Category;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer>{
    public Boolean existsByName(String name);
     
    public List<Category> findByIsActiveTrue();

}
