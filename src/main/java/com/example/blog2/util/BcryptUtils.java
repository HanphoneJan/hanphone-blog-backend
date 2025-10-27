package com.example.blog2.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 对字符串进行 Bcrypt 加密处理及验证
 * Bcrypt 是一种更安全的密码哈希算法，自带盐值且计算速度较慢，能有效抵御暴力破解
 */
public class BcryptUtils {

    /**
     * 加密密码
     * @param password 原始密码
     * @return 加密后的哈希值
     */
    public static String encrypt(String password) {
        // 生成随机盐值并加密，workFactor为12（默认值，可根据需要调整，值越大加密越慢，安全性越高）
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * 验证密码
     * @param password 原始密码
     * @param hashedPassword 加密后的哈希值
     * @return 验证成功返回true，否则返回false
     */
    public static boolean verify(String password, String hashedPassword) {
        // 注意：如果hashedPassword为null或空，会抛出异常，这里做一下保护
        if (password == null || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        return BCrypt.checkpw(password, hashedPassword);
    }

    /**
     * 测试主函数，用于验证加密和验证功能
     */
    public static void main(String[] args) {
        // 测试用的原始密码
        String originalPassword = "e10adc3949ba59abbe56e057f20f883e";
        System.out.println("原始密码: " + originalPassword);

        // 加密密码
        String encryptedPassword = encrypt(originalPassword);
        System.out.println("加密后的密码: " + encryptedPassword);

        // 验证正确的密码
        boolean isMatch = verify(originalPassword, encryptedPassword);
        System.out.println("正确密码验证结果: " + isMatch);

        // 验证错误的密码
        String wrongPassword = "WrongPassword123!";
        boolean isWrongMatch = verify(wrongPassword, encryptedPassword);
        System.out.println("错误密码验证结果: " + isWrongMatch);

        // 测试边界情况：空密码
        String emptyPassword = "";
        String encryptedEmpty = encrypt(emptyPassword);
        System.out.println("空密码加密结果: " + encryptedEmpty);
        System.out.println("空密码验证结果: " + verify(emptyPassword, encryptedEmpty));

        // 测试null值
        System.out.println("null密码验证结果: " + verify(null, encryptedPassword));
        System.out.println("null哈希值验证结果: " + verify(originalPassword, null));
    }
}
