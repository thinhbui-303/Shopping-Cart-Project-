package com.ecom.shopping_cart.service.impl;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.nio.file.Path;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import org.springframework.web.multipart.MultipartFile;

import com.ecom.shopping_cart.model.Product;
import com.ecom.shopping_cart.repository.ProductRepository;
import com.ecom.shopping_cart.service.interf.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        product.setDiscountPrice(product.getPrice());
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Boolean deleteProductById(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        if (!ObjectUtils.isEmpty(product)) {
            productRepository.delete(product);
            return true;
        }
        return false;

    }

    @Override
    public Product getProductById(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        return product;
    }

    @Override
    public Product updateProduct(Product product, MultipartFile file) {
        Product updateProduct = getProductById(product.getId());
        String imageName = file.isEmpty() ? product.getImage() : file.getOriginalFilename();
        updateProduct.setTitle(product.getTitle());
        updateProduct.setDescription(product.getDescription());
        updateProduct.setPrice(product.getPrice());
        updateProduct.setStock(product.getStock());
        updateProduct.setIsActive(product.getIsActive());
        updateProduct.setDiscount(product.getDiscount());

        double discountPrice = product.getPrice() * (1 - (product.getDiscount() / 100));
        updateProduct.setDiscountPrice(discountPrice);
        saveProduct(updateProduct);
        if (imageName != null) {
            try {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "products_img" + File.separator
                        + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return product;
    }

    @Override
    public List<Product> getAllIsActiveProduct(String category) {
        List<Product> products = null;
        if (ObjectUtils.isEmpty(category)) {
            products = productRepository.findByIsActiveTrue();
        } else {
            products = productRepository.findByCategory(category);
        }
        return products;

    }

    @Override
    public List<Product> searchProduct(String ch){
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
    }
    @Override
    public Page<Product> getAllIsActiveProductPagination(Integer pageNo, Integer pageSize, String category){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Product> products = null;
        if(category == null){
            products = productRepository.findByIsActiveTrue(pageable);
        }
        else{
            products = productRepository.findByCategory(pageable ,category);
        }
        
        return products;
    }
    @Override
    public Page<Product> getAllProductPagination(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return productRepository.findAll(pageable);
    }
    @Override
    public Page<Product> searchProductPagination(String ch , Integer pageNo , Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch, pageable);
        }

    @Override
    public Page<Product> searchIsActiveProductPagination(String ch , Integer pageNo, Integer pageSize, String category){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return productRepository.findByIsActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch, pageable);
    }

}
