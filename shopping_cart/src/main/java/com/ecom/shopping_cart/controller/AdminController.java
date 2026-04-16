package com.ecom.shopping_cart.controller;

import com.ecom.shopping_cart.model.Category;
import com.ecom.shopping_cart.model.Product;
import com.ecom.shopping_cart.model.ProductOrder;
import com.ecom.shopping_cart.model.UserDtls;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.shopping_cart.service.interf.CategoryService;
import com.ecom.shopping_cart.service.interf.OrderService;
import com.ecom.shopping_cart.service.interf.ProductService;
import com.ecom.shopping_cart.service.interf.UserService;
import com.ecom.shopping_cart.util.ComonUtil;
import com.ecom.shopping_cart.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @Autowired
    private ComonUtil comonUtil;

    @ModelAttribute
    public void getUserDetails(Principal p, Model model) {
        if (p != null) {
            String email = p.getName();
            UserDtls user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
        }
        model.addAttribute("categories", categoryService.getAllIsActiveCategory());

    }

    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Integer id, HttpSession session) {
        if (categoryService.deleteCategory(id) == true)
            session.setAttribute("successMsg", "delete succcess!");
        else
            session.setAttribute("errorMsg", "there something wrong!");

        return "redirect:/admin/category";
    }

    @GetMapping("/category")
    public String category(Model model, @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        Page<Category> page = categoryService.getAllCategoryPagination(pageNo, pageSize);
        model.addAttribute("categories", page.getContent());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("totalCategories", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());

        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
            HttpSession session) throws IOException {

        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);

        Boolean existCategory = categoryService.existCategory(category.getName());

        if (existCategory) {
            session.setAttribute("errorMsg", "Category Name already exists");
        } else {

            Category saveCategory = categoryService.saveCategory(category);

            if (ObjectUtils.isEmpty(saveCategory)) {
                session.setAttribute("errorMsg", "Not saved ! internal server error");
            } else {

                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "categories_img" + File.separator
                        + file.getOriginalFilename());

                // System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                session.setAttribute("successMsg", "Saved successfully");
            }
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));

        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
            HttpSession session) throws IOException {
        Category oldCategory = categoryService.getCategoryById(category.getId());
        String imgName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

        if (!ObjectUtils.isEmpty(category)) {
            oldCategory.setName(category.getName());
            oldCategory.setIsActive(category.getIsActive());
            oldCategory.setImageName(imgName);
        }
        Category updateCategory = categoryService.saveCategory(oldCategory);
        if (updateCategory != null) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "categories_img" + File.separator
                        + file.getOriginalFilename());

                // System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("successMsg", "Update succcess!");

        } else
            session.setAttribute("errorMsg", "Somthing wrong happen!");
        return "redirect:/admin/loadEditCategory/" + category.getId();
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);

        return "admin/add_product";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile imagFile,
            HttpSession session)
            throws IOException {
        String imageName = imagFile.isEmpty() ? "default.jpg" : imagFile.getOriginalFilename();
        product.setImage(imageName);
        Product saveProduct = productService.saveProduct(product);

        if (!ObjectUtils.isEmpty(saveProduct)) {
            File saveFile = new ClassPathResource("static/img").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "products_img" + File.separator
                    + imagFile.getOriginalFilename());
            Files.copy(imagFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            session.setAttribute("successMsg", "Add product success!");
        } else {

            session.setAttribute("errorMsg", "Something wrong!");

        }
        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("/products")
    public String loadViewProducts(Model model, @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,@RequestParam(defaultValue = "") String ch , HttpSession session) {

        Page<Product> page = null;
        if(ch != null && ch.length() > 0){
            page =productService.searchProductPagination(ch, pageNo, pageSize); 
        }
        else{
            session.setAttribute("errorMsg", "Invalid infor product!");
            page = productService.getAllProductPagination(pageNo, pageSize);
        }

        model.addAttribute("products", page.getContent());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("ch", ch);
        model.addAttribute("totalProducts", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        return "admin/products";
    }

    @GetMapping("/products/{id}")
    public String deleteProductById(@PathVariable Integer id, HttpSession session) {
        if (productService.deleteProductById(id)) {
            session.setAttribute("successMsg", "Product deleted successfully!");
        } else {
            session.setAttribute("successMsg", "Something wrong on server!");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/loadEditProduct/{id}")
    public String loadEditProduct(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(HttpSession session, @ModelAttribute Product product,
            @RequestParam("file") MultipartFile file) {
        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            session.setAttribute("errorMsg", "Invalid discount!");
        } else {
            Product updProduct = productService.updateProduct(product, file);

            if (!ObjectUtils.isEmpty(updProduct)) {
                session.setAttribute("successMsg", "Success edit product!");
            } else
                session.setAttribute("errorMsg", "Failed to edit product!");

        }
        return "redirect:/admin/loadEditProduct/" + product.getId();
    }

    @GetMapping("/users")
    public String getAllUsers(Model model,@RequestParam Integer type,
        @RequestParam(defaultValue = "0") Integer pageNo ,
        @RequestParam(defaultValue = "5") Integer pageSize
    ) {
        Page<UserDtls> page = null;
        if(type == 1){
        page = userService.getAllUserByRolePagination(pageNo, pageSize, "ROLE_USER");
        }
        else{
        page = userService.getAllUserByRolePagination(pageNo, pageSize, "ROLE_ADMIN");
        }
        model.addAttribute("users", page.getContent());
        model.addAttribute("type", type);
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("totalUsers", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        return "admin/users" ;
    }

    @GetMapping("/updateStatus")
    public String updateStatusUser(HttpSession session, @RequestParam Boolean status, @RequestParam Integer id, @RequestParam Integer type) {
        Boolean updateUser = userService.updateStatusUser(status, id);
        if (updateUser) {
            session.setAttribute("successMsg", "Update status user successful!");
        } else {
            session.setAttribute("errorMsg", "Something wrong on server !");

        }
        return "redirect:/admin/users?type=" + type;
    }

    @GetMapping("/adminOrder")
    public String adminOrder(Model model, @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        Page<ProductOrder> page = orderService.getAllOrderPagination(pageNo, pageSize);
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("totalOrders", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("orders", page.getContent());
        model.addAttribute("search", false);
        return "admin/order";
    }

    @PostMapping("/updateStatusOrder")
    public String updateStatusOrder(@RequestParam Integer st, @RequestParam(value = "id", required = false) Integer id,
            HttpSession session) {
        OrderStatus[] orderSt = OrderStatus.values();
        String status = null;
        for (OrderStatus orderStatus : orderSt) {
            if (orderStatus.getId() == st) {
                status = orderStatus.getName();
            }
        }
        ProductOrder check = orderService.updateStatusOrder(id, status);
        try {
            comonUtil.sendMailForProductOrder(status, check);
        } catch (Exception e) {
            e.getStackTrace();
        }
        if (!ObjectUtils.isEmpty(check)) {
            session.setAttribute("successMsg", "Change Order Status Successfully!");
        } else {
            session.setAttribute("errorMsg", "Change Order Status Failure!");
        }
        return "redirect:/admin/adminOrder";
    }

    @GetMapping("/searchProductOrder")
    public String searchProductOrder(@RequestParam String orderId, HttpSession session, Model model) {
        ProductOrder order = orderService.searchByOrderId(orderId.trim());
        if (!ObjectUtils.isEmpty(order)) {
            model.addAttribute("order", order);
            model.addAttribute("search", true);
        } else {
            session.setAttribute("errorMsg", "Order id is invalid!");
            model.addAttribute("search", false);
            model.addAttribute("orders", orderService.getAllOrder());

        }

        return "admin/order";
    }
    @GetMapping("/addAdmin")
    public String addAdmin() {
        return "admin/add_admin";
    }
    @PostMapping("/saveAdmin")
    public String saveAdmin(@ModelAttribute UserDtls userDtls, HttpSession session,
            @RequestParam("img") MultipartFile file) throws IOException {

        String imageName = !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
        userDtls.setProfileImage(imageName);

        UserDtls saveUserDtls = userService.saveAdmin(userDtls);
        if (!ObjectUtils.isEmpty(saveUserDtls)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profiles_img" + File.separator
                        + file.getOriginalFilename());

                // System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            }
            session.setAttribute("successMsg", "Add Admin successfully");

        } else
            session.setAttribute("errorMsg", "somthing wrong !");
        return "redirect:/admin/addAdmin";
    }
    @GetMapping("/profileAdmin")
    public String profileAdmin(){

        return "admin/profile_admin";
    }
    @PostMapping("/updateProfileAdmin")
    public String updateProfileAdmin(@RequestParam("img") MultipartFile file, HttpSession session, @ModelAttribute UserDtls userDtls) {
        UserDtls adminUpdate = userService.updateProfileUser(userDtls, file);
        if(!ObjectUtils.isEmpty(adminUpdate)){
            session.setAttribute("successMsg", "Update succcessful!");
        }
        else{
            session.setAttribute("errorMsg", "Something wrong on server !");
        }
        return "redirect:/admin/profileAdmin";
    }
    @PostMapping("/changePasswordAdmin")
    public String changePasswordAdmin(@RequestParam String newPassword, @RequestParam String currentPassword,
         HttpSession session, Principal p ){
            UserDtls currentAdmin = userService.getUserByEmail(p.getName());
            boolean check  = passwordEncoder.matches(currentPassword, currentAdmin.getPassword());
            if(check){
                String encodePassword = passwordEncoder.encode(newPassword);
                currentAdmin.setPassword(encodePassword);
                UserDtls updateAdmin = userService.updateUser(currentAdmin);
                if(!ObjectUtils.isEmpty(updateAdmin)){
                    session.setAttribute("successMsg", "Change Password succesfully!");
                }
                else{
                    session.setAttribute("errorMsg","Something wrong on server!");
                }
            }
            else{
                session.setAttribute("errorMsg", "Password is not matched!");
            }
        return "redirect:/admin/profileAdmin";
    }
    

   

}
