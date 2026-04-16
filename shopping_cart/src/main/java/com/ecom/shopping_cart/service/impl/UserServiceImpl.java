package com.ecom.shopping_cart.service.impl;

import com.ecom.shopping_cart.repository.UserRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.shopping_cart.model.UserDtls;
import com.ecom.shopping_cart.service.interf.UserService;
import com.ecom.shopping_cart.util.AppConstant;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDtls saveUser(UserDtls user) {
        user.setRole("ROLE_USER");
        user.setIsEnable(true);
        String pwEncoder = passwordEncoder.encode(user.getPassword());
        user.setPassword(pwEncoder);
        user.setFailureAttemp(0);
        user.setIsAccountNonLock(true);
        UserDtls saveUser = userRepository.save(user);
        return saveUser;
    }

    @Override
    public UserDtls getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDtls> getAllUserByRole(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public Boolean updateStatusUser(Boolean status, Integer id) {
        Optional<UserDtls> findUser = userRepository.findById(id);
        if (findUser.isPresent()) {
            UserDtls user = findUser.get();
            user.setIsEnable(status);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void increaseFailureAttemp(UserDtls user) {
        int failedAttemp = user.getFailureAttemp() + 1;
        user.setFailureAttemp(failedAttemp);
        userRepository.save(user);
    }

    @Override
    public void lockUserAccount(UserDtls user) {
        user.setIsAccountNonLock(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    @Override
    public Boolean unlockAccountTimeExpired(UserDtls user) {
        long lockTime = user.getLockTime().getTime();
        long unlockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;
        long current = System.currentTimeMillis();
        if (unlockTime < current) {
            user.setIsAccountNonLock(true);
            user.setFailureAttemp(0);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void updateUserResetToken(String email, String resetToken) {
        UserDtls user = userRepository.findByEmail(email);
        user.setResetToken(resetToken);
        userRepository.save(user);
    }

    @Override
    public UserDtls getUserByToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }

    @Override
    public UserDtls updateUser(UserDtls user) {
        return userRepository.save(user);
    }

    @Override
    public UserDtls updateProfileUser(UserDtls userDtls, MultipartFile file) {
        UserDtls user = userRepository.findById(userDtls.getId()).get();
        if (!file.isEmpty()) {
            user.setProfileImage(file.getOriginalFilename());
        }
        if (!ObjectUtils.isEmpty(user)) {
            user.setFullName(userDtls.getFullName());
            user.setPhoneNumber(userDtls.getPhoneNumber());
            user.setAddress(userDtls.getAddress());
            user.setCity(userDtls.getCity());
            user.setState(userDtls.getState());
            user.setPinCode(userDtls.getPinCode());
            user = userRepository.save(user);
        }
        try {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profiles_img" + File.separator
                        + file.getOriginalFilename());

                // System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public UserDtls saveAdmin(UserDtls userDtls) {
        userDtls.setRole("ROLE_ADMIN");
        userDtls.setIsEnable(true);
        String pwEncoder = passwordEncoder.encode(userDtls.getPassword());
        userDtls.setPassword(pwEncoder);
        userDtls.setFailureAttemp(0);
        userDtls.setIsAccountNonLock(true);
        UserDtls saveUser = userRepository.save(userDtls);
        return saveUser;
    }
    @Override
    public Page<UserDtls> getAllUserByRolePagination(Integer pageNo, Integer pageSize, String role){
        Pageable pageable = PageRequest.of(pageNo, pageSize); 
        Page<UserDtls> users = userRepository.findByRole(pageable, role);
        return users;
    }
    @Override
    public Boolean existsEmail(String email){
        return userRepository.existsByEmail(email);
    }

  

}
