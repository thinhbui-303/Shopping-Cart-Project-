package com.ecom.shopping_cart.service.interf;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.shopping_cart.model.*;

@Service
public interface UserService {
    public UserDtls saveUser(UserDtls userDtls); 

    public UserDtls getUserByEmail(String email);

    public List<UserDtls> getAllUserByRole(String role);

    public Boolean updateStatusUser(Boolean status,Integer id);

    public void lockUserAccount(UserDtls user);

    public void increaseFailureAttemp(UserDtls user);

    public Boolean unlockAccountTimeExpired(UserDtls user);

    public void updateUserResetToken(String email, String token);

    public UserDtls getUserByToken(String token);

    public UserDtls updateUser(UserDtls userDtls); 

    public UserDtls updateProfileUser(UserDtls userDtls, MultipartFile file);

    public UserDtls saveAdmin(UserDtls userDtls);

    public Page<UserDtls> getAllUserByRolePagination(Integer pageNo, Integer pageSize, String role);
    
    public Boolean existsEmail(String email);
} 
