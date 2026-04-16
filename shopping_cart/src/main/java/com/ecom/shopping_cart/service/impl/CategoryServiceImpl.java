package com.ecom.shopping_cart.service.impl;

import com.ecom.shopping_cart.model.Category;
import com.ecom.shopping_cart.repository.CategoryRepository;
import com.ecom.shopping_cart.service.interf.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }
    @Override
    public Boolean existCategory(String name){
        return categoryRepository.existsByName(name);
    }
    @Override
    public Boolean deleteCategory(Integer id){
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null){
            categoryRepository.delete(category);
            return true;
        }
        return false;
    }
    @Override
    public Category getCategoryById(Integer id){
        Category category = categoryRepository.findById(id).orElse(null);
            return category;

    }
    @Override
    public List<Category> getAllIsActiveCategory( ){

        return categoryRepository.findByIsActiveTrue();
    }
    @Override
    public Page<Category> getAllCategoryPagination(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Category> page = categoryRepository.findAll(pageable);
        return page;
    }
}
