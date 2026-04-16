package com.ecom.shopping_cart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecom.shopping_cart.model.Cart;
import com.ecom.shopping_cart.model.OrderRequest;
import com.ecom.shopping_cart.model.ProductOrder;
import com.ecom.shopping_cart.model.UserDtls;
import com.ecom.shopping_cart.service.interf.CartService;
import com.ecom.shopping_cart.service.interf.CategoryService;
import com.ecom.shopping_cart.service.interf.OrderService;
import com.ecom.shopping_cart.service.interf.UserService;
import com.ecom.shopping_cart.util.ComonUtil;
import com.ecom.shopping_cart.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;






@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private CartService cartService;

    @Autowired
    private ComonUtil comonUtil;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home() {
        return "user/home";
    }
    
    @ModelAttribute
    public void getUserDetails(Principal p , Model model){
        if(p != null){
            String email = p.getName();
            UserDtls user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
            Integer countCart = cartService.getCountCart(user.getId());
            model.addAttribute("countCart", countCart);
        }
                model.addAttribute("categories", categoryService.getAllIsActiveCategory());

    }
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
        Cart saveCart = cartService.saveCart(pid, uid);
        if(ObjectUtils.isEmpty(saveCart)){
            session.setAttribute("errorMsg", "Somthing wrong on server!");
        }
        else{
            session.setAttribute("successMsg", "Add to card successfully!");
        }
        return "redirect:/product/"+ pid;
    }
    @GetMapping("/getCart")
    public String getCartByUser(Model model, Principal p) {
        UserDtls user = userService.getUserByEmail(p.getName());
        List<Cart> carts = cartService.getCartByUserId(user.getId());
        model.addAttribute("cart", carts);
        model.addAttribute("totalOrderPrice", cartService.getTotalOrderPrice(user.getId()));
        return "user/cart";
    }
    @GetMapping("/updateQuantity")
    public String updateQuantity(@RequestParam String sy,@RequestParam Integer cid) {
        cartService.updateQuantityCart(sy, cid);
        return "redirect:/user/getCart";
    }
    @GetMapping("/order")
    public String getOrder(Principal p , Model model) {
        UserDtls user = userService.getUserByEmail(p.getName());
        List<Cart> carts = cartService.getCartByUserId(user.getId());
        model.addAttribute("cart", carts);
        Double totalPrice = cartService.getTotalOrderPrice(user.getId());
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalOrderPrice", totalPrice + 126 + 74);
        return "user/order";
    }
    @PostMapping("/saveOrder")
    public String saveOrder(Principal p, @ModelAttribute OrderRequest request ) throws Exception{
        UserDtls user = userService.getUserByEmail(p.getName());
        Integer uid = user.getId();
        orderService.saveOrder(uid, request);
        return "redirect:/user/success";
    }
    @GetMapping("/success")
    public String success() {
        return "user/success";
    }
    @GetMapping("/userOrder")
    public String userOrder(Model model , Principal p) {
        UserDtls user = userService.getUserByEmail(p.getName());
        List<ProductOrder> orders = orderService.getOrderByUserId(user.getId());
        model.addAttribute("orders", orders);
        return "user/user_order";
    }
    @GetMapping("/updateStatusOrder")
    public String updateStatusOrder(@RequestParam Integer st, @RequestParam Integer id, HttpSession session) {
        OrderStatus[] orderSt = OrderStatus.values();
        String status = null;
        for (OrderStatus orderStatus : orderSt) {
            if(orderStatus.getId() == st){
                status = orderStatus.getName();
            }
        }
       ProductOrder check =  orderService.updateStatusOrder(id, status);
       try {
        comonUtil.sendMailForProductOrder(status, check);
       } catch (Exception e) {
        e.getStackTrace();
    }
        if(!ObjectUtils.isEmpty(check)){
            session.setAttribute("successMsg", "Cancel Order Successfully!");
        }
        else{
            session.setAttribute("errorMsg", "Cancel Order Failure!");

        }
        return "redirect:/user/userOrder";
    }
    @GetMapping("/profile")
    public String getProfileUser() {
        return "user/profile";
    }
    @PostMapping("/updateProfileUser")
    public String updateProfileUser(@RequestParam("img") MultipartFile file, HttpSession session, @ModelAttribute UserDtls userDtls) {
        UserDtls userUpdate = userService.updateProfileUser(userDtls, file);
        if(!ObjectUtils.isEmpty(userUpdate)){
            session.setAttribute("successMsg", "Update succcessful!");
        }
        else{
            session.setAttribute("errorMsg", "Something wrong on server !");
        }
        return "redirect:/user/profile";
    }
    @PostMapping("/changePasswordUser")
    public String changePasswordUser(@RequestParam String newPassword, @RequestParam String currentPassword,
         HttpSession session, Principal p ){
            UserDtls currentUser = userService.getUserByEmail(p.getName());
            boolean check  = passwordEncoder.matches(currentPassword, currentUser.getPassword());
            if(check){
                String encodePassword = passwordEncoder.encode(newPassword);
                currentUser.setPassword(encodePassword);
                UserDtls updateUser = userService.updateUser(currentUser);
                if(!ObjectUtils.isEmpty(updateUser)){
                    session.setAttribute("successMsg", "Change Password succesfully!");
                }
                else{
                    session.setAttribute("errorMsg","Something wrong on server!");
                }
            }
            else{
                session.setAttribute("errorMsg", "Password is not matched!");
            }
        return "redirect:/user/profile";
    }
    
    
    
    
    
    
    
    
    
}
