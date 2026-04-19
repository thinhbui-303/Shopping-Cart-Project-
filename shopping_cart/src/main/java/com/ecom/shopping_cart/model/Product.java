package com.ecom.shopping_cart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    @Column(length = 500)
    @Size(min = 3 , max = 500, message = "Title must contain from 3 to 500 characters!")
    @NotBlank(message = "Title mustn't be blank!")
    private String title ; 

    @Column(length = 5000)
    @NotBlank(message = "Description mustn't be blank!")
    @Size(min = 10 , max = 5000, message = "Description must contain from 10 to 5000 characters!")
    private String description;

    @NotBlank(message = "Category mustn't be blank!")
    private String category;

    @DecimalMin(value = "0.01", message = "Price must be bigger than 0!")
    private double price;

    
    @Min(value = 0, message = "Stock mustn't be at negative!")
    private int stock;

    private String image;

    @DecimalMin(value = "0", message = "Discount mustn't be at negative!")
    @DecimalMax(value = "100", message = "Discount no more than 100%")
    private double discount;

    @DecimalMin(value = "0.01", message = "Price after discount must bigger than 0!")
    private double discountPrice;

    private Boolean isActive;

}
