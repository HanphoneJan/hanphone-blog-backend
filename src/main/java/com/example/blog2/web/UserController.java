package com.example.blog2.web;

import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.po.User;
import com.example.blog2.service.EmailCaptchaService;
import com.example.blog2.service.UserService;
import com.example.blog2.util.BcryptUtils;
import com.example.blog2.util.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@RestController
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final EmailCaptchaService emailCaptchaService;

    public UserController(UserService userService, EmailCaptchaService emailCaptchaService) {
        this.userService = userService;
        this.emailCaptchaService = emailCaptchaService;
    }

    // 登录接口：返回Map<String, Object>类型数据，泛型指定为Map<String, Object>
    @PostMapping(value = "/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, User> para) {
        User u = para.get("user");
        System.out.println(u);
        User user = userService.checkUser(u.getUsername(), u.getPassword());
        System.out.println(user);
        if (user != null) {
            TokenUtil.TokenInfo token = TokenUtil.sign(user);
            Map<String, Object> info = new HashMap<>();
            user.setLoginProvince(u.getLoginProvince());
            user.setLoginCity(u.getLoginCity());
            user.setLoginLat(u.getLoginLat());
            user.setLoginLng(u.getLoginLng());
            user.setLastLoginTime(new Date());
            User newUser = userService.save(user);

            info.put("user", newUser);
            info.put("token", Objects.requireNonNull(token).getToken());
            info.put("expire", token.getExpireTime());

            return new Result<>(true, StatusCode.OK, "登录成功", info);
        } else {
            return new Result<>(true, StatusCode.ERROR, "登录失败", null);
        }
    }

    // 注册接口：返回Map<String, Object>类型数据，泛型指定为Map<String, Object>
    @PostMapping(value = "/register")
    public Result<Map<String, Object>> post(@RequestBody Map<String, User> para)  {
        User u = para.get("user");
        boolean isExists = userService.isUserExists(u.getUsername().trim(), u.getEmail().trim());
        if (isExists) {
            return new Result<>(false, StatusCode.ERROR, "用户名或邮箱已被占用", null);
        }
        String encryptPassword = BcryptUtils.encrypt(u.getPassword());
        u.setPassword(encryptPassword);
        User user = userService.save(u);
        TokenUtil.TokenInfo tokenInfo = TokenUtil.sign(user);
        Map<String, Object> info = new HashMap<>();
        info.put("user", user);
        info.put("token", Objects.requireNonNull(tokenInfo).getToken());
        info.put("expire", tokenInfo.getExpireTime());
        return new Result<>(true, StatusCode.OK, "注册并登录成功", info);
    }

    // 重置密码接口：无具体数据返回（null），泛型指定为Void
    @PostMapping(value="/user/resetPassword")
    public Result<Void> resetPassword(@RequestBody Map<String,String> para) {
        try{
            String newPassword = para.get("newPassword");
            String captcha = para.get("captcha");
            String email = para.get("email");
            if(emailCaptchaService.validateCaptcha(email,captcha)){
                if(userService.resetPassword(newPassword,email)){
                    return new Result<>(true, StatusCode.OK, "重置密码成功", null);
                }
                return new Result<>(true, StatusCode.ERROR, "重置密码失败", null);
            }
            return new Result<>(true, StatusCode.ERROR, "重置密码失败", null);
        }catch (Exception e){
            return new Result<>(true, StatusCode.ERROR, "重置密码失败", null);
        }
    }

    // 发送验证码接口：无具体数据返回（null），泛型指定为Void
    @PostMapping(value="/user/sendCaptcha")
    public Result<Void> sendCaptcha(@RequestBody Map<String,String> para) {
        try{
            String email = para.get("email");
            if (email == null || email.trim().isEmpty()) {
                return new Result<>(false, StatusCode.ERROR, "邮箱地址不能为空", null);
            }
            if(emailCaptchaService.sendCaptcha(email)){
                return new Result<>(true, StatusCode.OK, "发送验证码成功", null);
            }
            return new Result<>(true, StatusCode.ERROR, "发送验证码失败", null);
        }catch (Exception e){
            return new Result<>(true, StatusCode.ERROR, "发送验证码失败", null);
        }
    }

    @GetMapping("/logout")
    public Result<Void>  logout(HttpSession session){
        session.removeAttribute("user");
        return new Result<>(true, StatusCode.OK, "退出登录成功", null);
    }
}