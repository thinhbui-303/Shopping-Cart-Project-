package com.ecom.shopping_cart.controller;

import com.ecom.shopping_cart.model.Category;
import com.ecom.shopping_cart.model.Product;
import com.ecom.shopping_cart.model.UserDtls;
import com.ecom.shopping_cart.service.impl.FileStorageService;
import com.ecom.shopping_cart.service.interf.*;
import com.ecom.shopping_cart.util.ComonUtil;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

// import java.nio.file.Files;
// import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
// import java.nio.file.Path;

// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CartService cartService;

    @Autowired
    private ComonUtil comonUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model model) {
        if (p != null) {
            String email = p.getName();
            UserDtls user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
            Integer counCart = cartService.getCountCart(user.getId());
            model.addAttribute("countCart", counCart);
        }
        model.addAttribute("categories", categoryService.getAllIsActiveCategory());
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Category> cates = categoryService.getAllIsActiveCategory().stream()
                .sorted((c1, c2) -> c2.getId().compareTo(c1.getId())).limit(6).toList();

        List<Product> products = productService.getAllIsActiveProduct("").stream()
                .sorted((p1, p2) -> p1.getId().compareTo(p2.getId())).limit(8).toList();
        model.addAttribute("cates", cates);
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/saveUser")
    public String saveUser(@Valid @ModelAttribute UserDtls userDtls, 
            HttpSession session,
            @RequestParam("img") MultipartFile file) throws IOException {
        
        String imageName = !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
        userDtls.setProfileImage(imageName);
        Boolean checkExistEmail = userService.existsEmail(userDtls.getEmail());

        if (checkExistEmail) {
            session.setAttribute("errorMsg", "Email was used");
        } else {
            UserDtls saveUserDtls = userService.saveUser(userDtls);
            if (!ObjectUtils.isEmpty(saveUserDtls)) {
                if (!file.isEmpty()) {
                    // File saveFile = new ClassPathResource("static/img").getFile();

                    // Path path = Paths.get(saveFile.getAbsolutePath() + File.separator +
                    // "profiles_img" + File.separator
                    // + file.getOriginalFilename());

                    // // System.out.println(path);
                    // Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    fileStorageService.saveFile(file, "profiles_img");
                }
                session.setAttribute("successMsg", "Register successfully");

            } else
                session.setAttribute("errorMsg", "somthing wrong !");
        }

        return "redirect:/register";
    }

    @GetMapping("/products")
    public String products(Model model, @RequestParam(required = false) String category,
            @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "12") Integer pageSize,
            @RequestParam(defaultValue = "") String ch) {

        model.addAttribute("categories", categoryService.getAllIsActiveCategory());
        model.addAttribute("paramValue", category);
        Page<Product> page = null;
        if (!StringUtils.isEmpty(ch)) {
            page = productService.searchIsActiveProductPagination(ch, pageNo, pageSize, category);
        } else {
            page = productService.getAllIsActiveProductPagination(pageNo, pageSize, category);
        }

        List<Product> products = page.getContent();
        model.addAttribute("products", products);
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("ch", ch);
        model.addAttribute("totalProducts", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        return "product";
    }

    @GetMapping("/searchProduct")
    public String searchProduct(@RequestParam String ch, Model model) {
        List<Product> products = productService.searchProduct(ch);
        List<Category> cates = categoryService.getAllIsActiveCategory();
        model.addAttribute("products", products);
        model.addAttribute("cates", cates);
        return "product";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "view_product";
    }

    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processforgotPassword(@RequestParam String email,
            HttpSession session, HttpServletRequest request) throws Exception, UnsupportedEncodingException {
        UserDtls user = userService.getUserByEmail(email);
        if (ObjectUtils.isEmpty(user)) {
            session.setAttribute("errorMsg", "Invalid email's user");
        } else {
            String resetToken = UUID.randomUUID().toString();
            userService.updateUserResetToken(email, resetToken);
            String url = ComonUtil.generateUrl(request) + resetToken;
            Boolean sendMail = comonUtil.sendEmail(email, url);
            if (sendMail) {
                session.setAttribute("successMsg", "Check email to reset pasword!");
            } else {
                session.setAttribute("errorMsg", "somthing wrong on server!");
            }
        }
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPassword(Model model, @RequestParam String resetToken, HttpSession session) {
        UserDtls user = userService.getUserByToken(resetToken);
        if (ObjectUtils.isEmpty(user)) {
            model.addAttribute("msg", "Your link is invalid or expired");
            return "message";
        }
        model.addAttribute("resetToken", resetToken);
        return "/reset_password";
    }

    @PostMapping("/reset-password")
    public String creataNewPassword(HttpSession session, @RequestParam String resetToken, @RequestParam String password,
            Model model) {
        UserDtls user = userService.getUserByToken(resetToken);
        if (ObjectUtils.isEmpty(user)) {
            model.addAttribute("msg", "Your link is invalid or expired");
            return "message";
        } else {
            user.setPassword(passwordEncoder.encode(password));
            user.setResetToken(null);
            userService.updateUser(user);
            model.addAttribute("msg", "Reset password successfully!Login again!");
        }
        return "message";
    }

}
