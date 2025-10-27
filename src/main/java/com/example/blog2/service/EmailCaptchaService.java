package com.example.blog2.service;

public interface EmailCaptchaService {
    Boolean sendCaptcha(String email);
    Boolean validateCaptcha(String email, String captcha);
}
