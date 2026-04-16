package com.ecom.shopping_cart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.shopping_cart.model.UserDtls;







public interface UserRepository extends JpaRepository<UserDtls, Integer>{
    public UserDtls findByEmail(String email);

    public UserDtls findByResetToken(String resetToken);

    public List<UserDtls> findByRole(String role);

    public Page<UserDtls> findByRole(Pageable pageable, String role);

   public Boolean existsByEmail(String email);
}
