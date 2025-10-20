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
 * 邮件验证码工具类。
 * <p>
 * 该工具类提供邮件验证码发送功能，支持HTML模板邮件和验证码的Redis缓存管理。
 * 主要功能包括：
 * <ul>
 *   <li>HTML邮件模板加载和参数替换</li>
 *   <li>验证码邮件发送（支持HTML格式）</li>
 *   <li>验证码缓存管理（默认5分钟有效期）</li>
 *   <li>Spring Boot邮件服务集成</li>
 * </ul>
 *
 * @author AFang Team
 * @version 1.0
 * @since 2022-11-20
 */
@Slf4j
@Component
public class EmailUtil {

    /**
     * Spring邮件发送器实例。
     */
    private final JavaMailSender mailSender;

    /**
     * 发件人邮箱地址，从Spring配置文件中读取。
     * 配置项：${spring.mail.username}
     */
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Redis工具类实例，用于验证码缓存管理。
     */
    @Resource
    private RedisUtil redisUtil;

    /**
     * 验证码过期时间常量（分钟）。
     */
    private static final Long EXPIRED_TIME = 5L;

    /**
     * 构造函数，通过依赖注入初始化邮件发送器。
     *
     * @param mailSender Spring邮件发送器实例
     */
    public EmailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 加载邮件模板并替换占位符。
     * <p>
     * 从classpath加载HTML邮件模板，并将模板中的占位符替换为实际的验证码和过期时间。
     * 支持的占位符：
     * <ul>
     *   <li>{{CODE}} - 验证码</li>
     *   <li>{{EXPIRATION_TIME}} - 过期时间描述</li>
     * </ul>
     *
     * @param code           验证码字符串
     * @param expirationTime 过期时间描述（如：5分钟）
     * @param templatePath   模板文件路径（相对于classpath）
     * @return 替换占位符后的HTML邮件内容
     * @throws IOException 当模板文件不存在或读取失败时抛出
     */
    public String getEmailTemplate(String code, String expirationTime, String templatePath) throws IOException {
// 创建资源加载对象，加载邮件模板
        ClassPathResource resource = new ClassPathResource(templatePath);
        if (!resource.exists()) {
            throw new IOException("模板文件不存在：" + templatePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            // 读取模板内容并按行拼接
            String template = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            // 替换占位符，链式调用替换两个占位符
            return template.replace("{{CODE}}", code).replace("{{EXPIRATION_TIME}}", expirationTime);
        }
    }

    /**
     * 发送验证码邮件。
     * <p>
     * 创建并发送HTML格式的验证码邮件，支持复杂邮件内容（包括HTML、附件等）。
     * 邮件发送成功或失败都会记录相应的日志信息。
     *
     * @param to              收件人邮箱地址
     * @param subject         邮件主题
     * @param contentTemplate HTML格式的邮件内容模板
     * @throws Exception 当邮件发送过程中出现错误时抛出
     */
    public void sendVerificationCodeEmail(String to, String subject, String contentTemplate) throws Exception {
        // 创建复杂邮件对象，支持HTML格式和其他类型
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 创建邮件消息帮助类，支持多部分和HTML内容
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        try {
            // 设置收件人
            mimeMessageHelper.setTo(to);
            // 设置邮件主题
            mimeMessageHelper.setSubject(subject);
            // 设置HTML格式的邮件正文
            mimeMessageHelper.setText(contentTemplate, true);
            // 设置发件人
            mimeMessageHelper.setFrom(fromEmail);

            // 发送邮件
            mailSender.send(mimeMessage);
            log.info("验证邮件发送成功，收件人：{}", to);

        } catch (Exception e) {
            log.error("邮件发送失败，收件人：{}，错误信息：{}", to, e.getMessage(), e);
            throw new MessagingException("邮件发送失败: " + e.getMessage(), e);
        }
    }

    /**
     * 发送验证码邮件（完整流程）。
     * <p>
     * 完整的验证码邮件发送流程，包括：
     * <ol>
     *   <li>生成6位数字验证码</li>
     *   <li>将验证码存储到Redis缓存中（5分钟有效期）</li>
     *   <li>加载HTML邮件模板并替换参数</li>
     *   <li>发送验证码邮件</li>
     * </ol>
     *
     * @param emailAddress 收件人邮箱地址
     * @throws Exception 当邮件发送过程中出现错误时抛出
     */
    public void sendEmailByCode(String emailAddress) throws Exception {

// 生成6位数字验证码
        String code = getVerificationCode();
        String subject = "验证码";

        // 将验证码存储到Redis缓存中，设置5分钟过期时间
        RedisUtil.set(emailAddress, code, EXPIRED_TIME, TimeUnit.MINUTES);

        // 加载HTML邮件模板并替换验证码和过期时间参数
        String contentTemplate = getEmailTemplate(code, EXPIRED_TIME + "分钟", "templates/email-verification-code.html");

        // 发送验证码邮件
        sendVerificationCodeEmail(emailAddress, subject, contentTemplate);
    }

}

