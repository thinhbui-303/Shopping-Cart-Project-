package com.ecom.shopping_cart.service.interf;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecom.shopping_cart.model.Category;

public interface CategoryService {
    public Category saveCategory(Category category);
    public List<Category> getAllCategory();
    public Boolean existCategory(String name);
    
    public Boolean deleteCategory(Integer id);

    public Category getCategoryById(Integer id);
    public List<Category> getAllIsActiveCategory();

    public Page<Category> getAllCategoryPagination(Integer pageNo, Integer pageSize);
}
