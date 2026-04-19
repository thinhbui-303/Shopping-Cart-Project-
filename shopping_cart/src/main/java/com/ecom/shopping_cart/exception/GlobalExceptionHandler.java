package com.ecom.shopping_cart.exception;



import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalExceptionHandler {
    

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, HttpSession session, HttpServletRequest request){
        String errorMessage = ex.getAllErrors().stream()
        .findFirst()
        .map(error -> error.getDefaultMessage())
        .orElse("Invalid data!");    
    session.setAttribute("errorMsg", errorMessage);
    String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ?referer:"/");
    }
   @ExceptionHandler(ResourceNotFoundException.class)
   public String handleResourceNotFoundException(ResourceNotFoundException ex, HttpSession session, HttpServletRequest request){
        session.setAttribute("errorMsg", ex.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null? referer : "/");
   }
   @ExceptionHandler(Exception.class)
   public String handleGeneralException(
           Exception ex,
           HttpServletRequest request,
           HttpSession session) {
       session.setAttribute("errorMsg", "Có lỗi xảy ra: " + ex.getMessage());
       String referer = request.getHeader("Referer");
       return "redirect:" + (referer != null ? referer : "/");
   }


   

    
}
