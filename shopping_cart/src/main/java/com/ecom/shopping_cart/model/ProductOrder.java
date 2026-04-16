package com.ecom.shopping_cart.model;

import java.time.LocalDate;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderId;

    private LocalDate orderDate;

    @ManyToOne
    private UserDtls user;

    @ManyToOne
    private Product product;

    private Integer quantity;

    private Double price;

    private String orderStatus;

    private String paymentType;

    @OneToOne(cascade = CascadeType.ALL)
    private OrderAddress orderAddress;
}
