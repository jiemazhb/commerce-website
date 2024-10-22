package com.alibou.notification.service;

import com.alibou.notification.entity.EmailTemplates;
import com.alibou.notification.entity.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.thymeleaf.context.Context;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sentPaymentSuccessEmail(String sentTo, String reciverName, BigDecimal amount, String orderReference) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        mimeMessageHelper.setFrom("Danny.H.Zhao@gmail.com");

        final String templateName = EmailTemplates.PAYMENT_CONFIRMATION.getTemplate();

        Map<String, Object> map = new HashMap<>();

        map.put("customerName", reciverName);
        map.put("amount", amount);
        map.put("orderReference", orderReference);

        Context context = new Context();
        context.setVariables(map);

        mimeMessageHelper.setSubject(EmailTemplates.PAYMENT_CONFIRMATION.getSubject());

        try{
            String htmlTemplate = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlTemplate, true);

            mimeMessageHelper.setTo(sentTo);
            mailSender.send(mimeMessage);
            log.info(String.format("info - email successfully sent to %s with template %s,", sentTo, templateName));
        }catch (Exception e){
            log.info(e.getMessage() + "邮件错误消息........");
            log.warn("waring - cannot sent email to {}", sentTo);
        }
    }
    @Async
    public void sentOrderConfirmationEmail(String sentTo, String reciverName, BigDecimal amount, String orderReference, List<Product> products) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        mimeMessageHelper.setFrom("Danny.H.Zhao@gmail.com");

        final String templateName = EmailTemplates.ORDER_CONFIRMATION.getTemplate();

        Map<String, Object> map = new HashMap<>();

        map.put("customerName", reciverName);
        map.put("totalAmount", amount);
        map.put("orderReference", orderReference);
        map.put("products", products);

        Context context = new Context();
        context.setVariables(map);

        mimeMessageHelper.setSubject(EmailTemplates.ORDER_CONFIRMATION.getSubject());

        try{
            String htmlTemplate = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlTemplate, true);

            mimeMessageHelper.setTo(sentTo);
            mailSender.send(mimeMessage);
            log.info(String.format("info - email successfully sent to %s with template %s,", sentTo, templateName));
        }catch (MessagingException e){
            log.warn("waring - cannot sent email to {}", sentTo);
        }
    }

}
