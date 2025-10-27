package com.example.blog2.service.impl;

import com.example.blog2.service.EmailCaptchaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class EmailCaptchaServiceImpl implements EmailCaptchaService {

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.from-name}")
    private String fromName;

    private static final int CAPTCHA_EXPIRE_MINUTES = 5;
    private static final int CAPTCHA_LENGTH = 6;

    // 构造函数依赖校验
    public EmailCaptchaServiceImpl(JavaMailSender javaMailSender, RedisTemplate<String, String> redisTemplate) {
        this.javaMailSender = Objects.requireNonNull(javaMailSender, "javaMailSender must not be null");
        this.redisTemplate = Objects.requireNonNull(redisTemplate, "redisTemplate must not be null");
    }

    @Override
    public Boolean sendCaptcha(String email) {
        try {
            // 校验输入参数
            Objects.requireNonNull(email, "email must not be null");
            // 校验配置参数
            Objects.requireNonNull(fromEmail, "fromEmail must not be null (check spring.mail.username config)");
            Objects.requireNonNull(fromName, "fromName must not be null (check spring.mail.from-name config)");

            // 生成验证码
            String captcha = generateCaptcha();
            Objects.requireNonNull(captcha, "generated captcha must not be null");

            // 构建邮件消息
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("【" + fromName + "的个人博客】验证码");
            message.setText("您的验证码是：" + captcha + "，有效期" + CAPTCHA_EXPIRE_MINUTES + "分钟，请尽快使用。");

            // 发送邮件
            javaMailSender.send(message);

            // 存储验证码到Redis
            String redisKey = "captcha:" + email;
            redisTemplate.opsForValue().set(redisKey, captcha, CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);

            return true;
        } catch (IllegalArgumentException e) {
            // 处理参数错误
            System.err.println("发送验证码参数错误: " + e.getMessage());
            return false;
        } catch (Exception e) {
            // 处理其他异常（邮件发送失败、Redis操作失败等）
            System.err.println("发送验证码失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean validateCaptcha(String email, String captcha) {
        try {
            // 校验输入参数
            Objects.requireNonNull(email, "email must not be null");
            Objects.requireNonNull(captcha, "captcha must not be null");

            String redisKey = "captcha:" + email;
            String storedCaptcha = redisTemplate.opsForValue().get(redisKey);

            // 验证验证码
            if (captcha.equals(storedCaptcha)) {
                redisTemplate.delete(redisKey);
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("验证码验证参数错误: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("验证码验证失败: " + e.getMessage());
            return false;
        }
    }

    private String generateCaptcha() {
        try {
            Random random = new Random();
            StringBuilder sb = new StringBuilder(CAPTCHA_LENGTH);

            for (int i = 0; i < CAPTCHA_LENGTH; i++) {
                sb.append(random.nextInt(10));
            }

            String captcha = sb.toString();
            Objects.requireNonNull(captcha, "generated captcha must not be null");
            if (captcha.length() != CAPTCHA_LENGTH) {
                throw new IllegalStateException("generated captcha length incorrect: " + captcha.length());
            }
            return captcha;
        } catch (Exception e) {
            throw new RuntimeException("failed to generate captcha", e);
        }
    }
}