package com.example.blog2.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.blog2.po.User;

import java.util.Date;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TokenUtil {

    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000; // 7天有效
    private static final String TOKEN_SECRET = "xxxxxxx"; // 密钥盐
    private static final Log log = LogFactory.getLog(TokenUtil.class);

    /**
     * 签名生成
     */
    @Getter
    public static class TokenInfo {
        // Getter方法
        private final String token;
        private final Date expireTime;

        public TokenInfo(String token, Date expireTime) {
            this.token = token;
            this.expireTime = expireTime;
        }

    }

    public static TokenInfo sign(User user) {
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("userId", user.getId().toString())
                    .withClaim("userType", user.getType())
                    .withExpiresAt(expiresAt)
                    // 使用HMAC256加密算法
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));

            return new TokenInfo(token, expiresAt);
        } catch (Exception e) {
            log.error("生成Token失败", e);
            return null;
        }
    }

    /**
     * 签名验证
     */
    public static boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("认证通过：");
            System.out.println("userId: " + jwt.getClaim("userId").asString());
            System.out.println("userType: " + jwt.getClaim("userType").asString());
            System.out.println("过期时间：      " + jwt.getExpiresAt());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 管理员认证
     */
    public static boolean adminVerify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            // System.out.println("管理员认证通过");
            return "1".equals(jwt.getClaim("userType").asString());
        } catch (Exception e) {
            return false;
        }
    }

}