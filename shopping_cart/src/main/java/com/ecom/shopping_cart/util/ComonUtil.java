package com.ecom.shopping_cart.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ecom.shopping_cart.model.ProductOrder;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class ComonUtil {
    @Autowired
    private JavaMailSender mailSender;

    public Boolean sendEmail(String email, String url) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("dauphongdz@gmail.com", "Shopping Card");
        helper.setTo(email);
        String content = "<p> Chào bạn, </p>" + "<p> Hãy bấm vào đường link này để xác nhận đổi mật khẩu: </p>"
                + "<a href=\"" + url + "\">Đổi mật khẩu ngay</a>";
        helper.setText(content, true);
        mailSender.send(message);
        return true;

    }

    public static String generateUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();

        return url.replace(request.getServletPath(), "/reset-password?resetToken=");
    }

    String msg = null;
    
    public Boolean sendMailForProductOrder(String status, ProductOrder productOrder) throws Exception {
        msg = "<p>Hello, [[fullName]]</p>"
                + "<p><b>Your order status is [[orderStatus]]</b></p>"
                + "<p><b>Product order details:</b></p>"
                + "<p>Category: [[category]]</p>"
                + "<p>Product Name: [[title]]</p>"
                + "<p>Quantity: [[quantity]]</p>"
                + "<p>Price: [[price]]</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("dauphongdz@gmail.com", "Shopping_Cart");
        helper.setTo(productOrder.getUser().getEmail());
        msg = msg.replace("[[fullName]]", productOrder.getUser().getFullName());
        msg = msg.replace("[[orderStatus]]", productOrder.getOrderStatus());
        msg = msg.replace("[[category]]", productOrder.getProduct().getCategory());
        msg = msg.replace("[[title]]", productOrder.getProduct().getTitle());

        msg = msg.replace("[[quantity]]", productOrder.getQuantity().toString());
        msg = msg.replace("[[price]]", productOrder.getPrice().toString());

        helper.setSubject("Product Order Status");
        helper.setText(msg, true);
        mailSender.send(message);
        return true;

    }
}
