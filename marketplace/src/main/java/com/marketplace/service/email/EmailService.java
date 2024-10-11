package com.marketplace.service.email;

import com.marketplace.model.Buyer;
import com.marketplace.model.Order;
import com.marketplace.model.OrderItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp);
        javaMailSender.send(message);
    }


    public void sendOrderConfirmationEmail(Order order) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("noreply@yourmarketplace.com");
        helper.setTo(order.getBuyer().getEmail());
        helper.setSubject("Order Confirmation - Order #" + order.getId());

        String emailContent = buildOrderConfirmationEmail(order);
        helper.setText(emailContent, true);

        javaMailSender.send(message);
    }

    private String buildOrderConfirmationEmail(Order order) throws IOException {
        String template = loadEmailTemplate();

        String orderItems = buildOrderItemsHtml(order);
        String buyerAddress = buildBuyerAddressHtml(order.getBuyer());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm:ss");

        return template
                .replace("${buyerName}", order.getBuyer().getFirstname() + " " + order.getBuyer().getLastname())
                .replace("${orderId}", order.getId().toString())
                .replace("${orderDate}", order.getOrderDate().format(formatter))
                .replace("${orderStatus}", order.getOrderStatus().toString())
                .replace("${paymentMethod}", order.getPaymentMethod().getPaymentType().toString())
                .replace("${orderItems}", orderItems)
                .replace("${totalAmount}", order.getTotalPrice().toString())
                .replace("${buyerAddress}", buyerAddress)
                .replace("${estimatedDeliveryDate}", order.getShipmentDetails().getEstimatedDeliveryDate().format(formatter));
    }

    private String buildOrderItemsHtml(Order order) {
        StringBuilder items = new StringBuilder();
        for (OrderItem item : order.getOrderItems()) {
            items.append("<tr>")
                    .append("<td>").append(item.getProduct().getName()).append("</td>")
                    .append("<td><img src='").append(item.getProduct().getImages().get(0).getImageUrl()).append("' alt='")
                    .append(item.getProduct().getName()).append("' class='product-image'></td>")
                    .append("<td>").append(item.getQuantity()).append("</td>")
                    .append("<td>$").append(item.getPrice()).append("</td>")
                    .append("<td>$").append(item.getPrice().multiply(new BigDecimal(item.getQuantity()))).append("</td>")
                    .append("</tr>");
        }
        return items.toString();
    }

    private String buildBuyerAddressHtml(Buyer buyer) {
        return buyer.getStreet() + "<br>" +
                buyer.getCity() + ", " + buyer.getState() + " " + buyer.getZipcode() + "<br>" +
                buyer.getCountry();
    }

    private String loadEmailTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/order-confirmation-template.html");
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return new String(bytes, StandardCharsets.UTF_8);
    }




}
