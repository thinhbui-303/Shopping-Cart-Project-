package com.ecom.shopping_cart.config;

import java.io.IOException;




import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.ecom.shopping_cart.model.UserDtls;
import com.ecom.shopping_cart.repository.UserRepository;
import com.ecom.shopping_cart.service.interf.UserService;
import com.ecom.shopping_cart.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler{
    @Autowired
    UserRepository userRepository;
    @Autowired
    @Lazy
    UserService userService;
@Override
    public void onAuthenticationFailure(HttpServletRequest request , HttpServletResponse response, AuthenticationException exception)
    throws IOException, ServletException{
       String email = request.getParameter("username");
       UserDtls user = userService.getUserByEmail(email);

      if(user != null){
         if(user.getIsEnable()){
            if (user.getIsAccountNonLock()) {
                if (user.getFailureAttemp() < AppConstant.ATTEMP_TIME) {
                    userService.increaseFailureAttemp(user);
                    exception = new LockedException("Account has 3 attemp time!");
                }
                else{
                    userService.lockUserAccount(user);
                    exception = new LockedException("Account has been locked!");
                }
            }
            else{
                if(userService.unlockAccountTimeExpired(user)){
                    exception = new LockedException("Account is unlock! login again!");
                }
                else{
                    exception = new LockedException("Account is locked! Wait a few minute!");
                }
            }
       }
       else {
        exception = new LockedException("Account is Disabled");
       }
      }
      else{
        exception = new LockedException("Account is invalid!");
      }
    super.setDefaultFailureUrl("/signin?error");
    super.onAuthenticationFailure(request, response, exception);
    }    
}
