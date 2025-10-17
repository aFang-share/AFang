package com.example.afanguserbackend.utils;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.afanguserbackend.utils.CommonUtil.getVerificationCode;

/**
 * @Author: aFang
 * @Date: 2022/11/20 14:39
 * @Time: 2022/11/20 14:39
 * 邮箱验证码工具类
 * */
@Slf4j
@Component
public class EmailUtil {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Resource
    private RedisUtil redisUtil;
    private static final Long EXPIRED_TIME = 5L;

    public EmailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    /**
     * 邮件模板加载
     */
    public String getEmailTemplate(String code, String expirationTime, String templatePath) throws IOException {
//新建资源加载对象，会存对应的path
        ClassPathResource resource = new ClassPathResource(templatePath);
        if (!resource.exists()) {
            throw new IOException("模板文件不存在：" + templatePath);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String template = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            // 替换占位符，链式调用替换两个占位符
            return template.replace("{{CODE}}", code).replace("{{EXPIRATION_TIME}}", expirationTime);
        }

    }

    /**
     * 验证码邮件模板生成
     */
    public void sendVerificationCodeEmail(String to, String subject, String contentTemplate) throws Exception {
        // 邮件逻辑
        MimeMessage mimeMessage = mailSender.createMimeMessage();//创建复杂邮件对象支持html以及其他类型
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");//复杂邮件对象帮助类
        try {
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            // 设置 HTML 正文
            mimeMessageHelper.setText(contentTemplate, true);
            mimeMessageHelper.setFrom(fromEmail);
            // 发送邮件
            mailSender.send(mimeMessage);
            log.info("验证邮件发送成功，收件人：{}", to);
        } catch (Exception e) {
            log.error("邮件发送失败，收件人：{}，错误信息：{}", to, e.getMessage());
            throw new MessagingException("邮件发送失败: " + e.getMessage());
        }

    }

    /**
     * 发送邮件
     */
    public void sendEmailByCode(String emailAdress) throws Exception {

//        我这里不加入5分钟内Redis用户已生成验证码验证和重复利用
        String code = getVerificationCode();
        String subject = "验证码";
        //        验证码过期+redis
        RedisUtil.set(emailAdress, code, EXPIRED_TIME, TimeUnit.MINUTES);
        String contentTemplate = getEmailTemplate(code, EXPIRED_TIME+"分钟", "templates/email-verification-code.html");
        sendVerificationCodeEmail(emailAdress, subject, contentTemplate);
    }

}

