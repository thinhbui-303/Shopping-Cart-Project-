package com.ecom.shopping_cart.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserDtls {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ; 

    @NotBlank(message = "Name must not be blank!")
    @Size(min = 3 , max = 100 , message = "Name must be at least 3 characters and no more than 100 characters!" )
    private String fullName;

    @NotNull(message = "Phone number mustn't be blank!")
    @Pattern(regexp = "^[0-9]{10}$", message =  "phone number must 10 numbers")
    private String phoneNumber;

    @Email(message = "Email is invalid!")
    @NotBlank(message = "Email mustn't be blank!")
    private String email; 

    @NotBlank(message = "Address mustn't be blank!")
    @Size(min = 5 , max = 200, message = "Address must contain from 5 to 200 characters!")
    private String address;

    @NotBlank(message = "City mustn't be blank!")
    private String city;

    @NotBlank(message = "State mustn't be blank!")
    private String state;

    @NotBlank(message = "Pincode mustn't be blank!")
    @Pattern(regexp = "^[0-9]{5,6}$", message = "Pincode must only contain from 5 to 6 numbers!")
    private String pinCode;

    @NotBlank(message = "Password mustn't be blank!")
    @Size(min = 6, max = 50, message = "Password must contain from 6 to 50 characters!")
    private String password;
    private String profileImage;
   
    private String role;
    private Boolean isEnable;

    private Boolean isAccountNonLock;
    private Integer failureAttemp;
    private Date lockTime;
    private String resetToken;
}
